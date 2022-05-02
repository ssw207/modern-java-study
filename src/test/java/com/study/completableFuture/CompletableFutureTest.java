package com.study.completableFuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CompletableFutureTest {

    @Test
    public void supplyAsync() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message)); // 비동기로 실행

        /* when */
        String result = messageFuture.join(); // 블렁킹. 검증을 위해 messageFuture가 끝나기를 기다린다.

        /* then */
        assertThat(result).isEqualTo("Say Hello");
    }

    @Test
    void runAsync() {
        /* given */
        String message = "Hello";
        CompletableFuture<Void> messageFuture = CompletableFuture.runAsync(() -> sayMessage(message));
        /* when */

        /* then */
        messageFuture.join(); // 블럭킹. 비동기 실행의 리턴값을 받지 않는다.
    }

    @Test
    void completedFuture() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.completedFuture(message); // 정적값을 랩핑하기 위해 사용한다.(왜?)

        /* when */
        String result = messageFuture.join();

        /* then */
        assertThat(result).isEqualTo("Hello");
    }

    @Test
    void thenApply() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture
                .thenApply(saidMessage -> "Applied message : " + saidMessage) //비동기 실행결과를 받아 후처리한다.
                .join(); // 블럭킹후 결과를 리턴한다.

        /* then */
        assertThat(result).isEqualTo("Applied message : Say Hello");
    }

    @Test
    void thenAccept() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */ /* then */
        messageFuture.thenAccept(saidMessage -> {

            String acceptedMessage = "accepted message : " + saidMessage;

            log.info("thenAccept {}", acceptedMessage);
        }).join(); // 블럭킹. 비동기 실행결과를 후처리한다. 리턴값은 없다.
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
