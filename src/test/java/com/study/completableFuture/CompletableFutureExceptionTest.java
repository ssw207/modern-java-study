package com.study.completableFuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class CompletableFutureExceptionTest {

    @Test
    void exceptionally() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessageThrowingException(message));

        /* when */
        String result = messageFuture.exceptionally(Throwable::getMessage).join(); // messageFuture 예외 발생시 예외처리함

        /* then */
        assertThat(result).isEqualTo("java.lang.IllegalStateException: exception message");
    }

    @DisplayName("CompletableFuture.handle() -> 정상과 예외처리를 종합적으로 처리한다.")
    @Test
    void handle() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture.handle((saidMessage, throwable) -> "Applied message : " + saidMessage).join(); //에러시 saidMessage null, 정상처리시throwable null이 전달됨

        /* then */
        assertThat(result).isEqualTo("Applied message : Say Hello");
    }

    // =======================================================================

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

    private String sayMessageThrowingException(String message) {
        sleepOneSecond();
        throw new IllegalStateException("exception message");
    }
}
