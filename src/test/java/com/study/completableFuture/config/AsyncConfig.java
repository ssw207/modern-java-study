package com.study.completableFuture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync // @Async 어노테이션을 감지 어노테이션에 지정한 이름으로 쓰레드풀을 찾아 이용한다
@Configuration
public class AsyncConfig {

    public static final String LEARNING_DEFAULT_EXECUTOR_NAME = "threadPoolTaskExecutor";
    private static final int POOL_SIZE = 3;

    @Bean(LEARNING_DEFAULT_EXECUTOR_NAME)
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(POOL_SIZE);
        executor.setMaxPoolSize(POOL_SIZE);
        executor.setThreadNamePrefix("learning-thread-"); // 쓰레드풀의 이름을 지정한다.
        return executor;
    }
}
