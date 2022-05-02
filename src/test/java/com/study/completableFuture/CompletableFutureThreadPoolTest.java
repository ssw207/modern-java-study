package com.study.completableFuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class CompletableFutureThreadPoolTest {

    @Autowired
    private Executor threadPoolTaskExecutor;

    @DisplayName("thenApply() : 처음 진행한 스레드가 쭉 이어서 진행한다.")
    @Test
    void thenApplyWithSameThread() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message), threadPoolTaskExecutor); // 사용할 쓰레드 풀을 지정한다.

        /* when */
        String result = messageFuture.thenApply(saidMessage -> {
            log.info("thenApply() : Same Thread");
            return "Applied message : " + saidMessage;
        }).join();

        /* then */
        assertThat(result).isEqualTo("Applied message : Say Hello");
    }

    @DisplayName("thenApplyAsync() : 스레드 풀을 지정하지 않으면 기본 스레드 풀의 새로운 스레드가 async하게 진행한다.")
    @Test
    void thenApplyAsync() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message), threadPoolTaskExecutor);

        /* when */
        String result = messageFuture.thenApplyAsync(saidMessage -> { // thenApplyAsync 작업은 다른 쓰레드로 동작한다
            log.info("thenApplyAsync() : New thread in another thread pool");
            return "Applied message : " + saidMessage;
        }).join();

        /* then */
        assertThat(result).isEqualTo("Applied message : Say Hello");
    }

    @DisplayName("handleAsync() : 지정한 스레드 풀의 새로운 스레드가 async하게 진행한다.")
    @Test
    void thenApplyAsyncWithAnotherThreadPool() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message), threadPoolTaskExecutor);

        /* when */
        String result = messageFuture.thenApplyAsync(saidMessage -> {
            log.info("thenApplyAsync() : New thread in same thread pool");
            return "Applied message : " + saidMessage;
        }, threadPoolTaskExecutor).join(); // 쓰레드풀을 지정하면 지정한쓰레드풀에서 다른쓰레드로 동작한다.

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
}
