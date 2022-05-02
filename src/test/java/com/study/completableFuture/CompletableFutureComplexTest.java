package com.study.completableFuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CompletableFutureComplexTest {

    @Test
    void allOf() {
        /* given */
        List<String> messages = Arrays.asList("Hello", "Hi", "Bye", "Yes", "No");

        List<CompletableFuture<String>> messageFutures = messages
                .stream()
                .map(message -> CompletableFuture.supplyAsync(() -> this.sayMessage(message)))
                .collect(Collectors.toList()); // 비동기 실행

        /* when */
        List<String> saidMessages = CompletableFuture
                .allOf(messageFutures.toArray(new CompletableFuture[0])) // 여러 CompletableFuture 블럭킹
                .thenApply(Void -> messageFutures.stream().map(CompletableFuture::join).collect(Collectors.toList())) // 후처리
                .join();

        /* then */
        List<String> expectedMessages = Arrays.asList("Say Hello", "Say Hi", "Say Bye", "Say Yes", "Say No");
        assertThat(expectedMessages.equals(saidMessages)).isTrue();
    }

    @Test
    void thenCompose() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture.thenCompose(saidMessage -> CompletableFuture.supplyAsync(() -> { // CompletableFuture 파이프라인실해
            sleepOneSecond();
            return saidMessage + "!";
        })).join();

        /* then */
        assertThat(result).isEqualTo("Say Hello!");
    }

    @Test
    void thenCombine() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture.thenCombine(CompletableFuture.supplyAsync(() -> { //CompletableFuture 파이프라인 실행후 후처리
            sleepOneSecond();
            return "!";
        }), (message1, message2) -> message1 + message2).join(); /* then */
        assertThat(result).isEqualTo("Say Hello!");
    }

    private String sayMessage(String message) {
        sleepOneSecond();
        String saidMessage = "Say " + message;
        log.info("Said Message = {}", saidMessage);
        return saidMessage;
    }

    private void sleepOneSecond() {
        try {
            log.info("start to sleep 1 second.");
            Thread.sleep(1000);
            log.info("end to sleep 1 second.");
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
    }
}
