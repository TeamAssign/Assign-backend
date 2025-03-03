package com.team3.assign_back.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {
    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(
                2,  // 코어 스레드 개수
                4,  // 최대 스레드 개수
                60L, TimeUnit.SECONDS,  // 유휴 스레드 유지 시간
                new LinkedBlockingQueue<>(50), //
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy() //최대스레드 + 큐 꽉차면 에러
        );
    }
}