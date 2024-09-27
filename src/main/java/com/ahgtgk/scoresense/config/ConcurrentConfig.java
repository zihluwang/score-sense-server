package com.ahgtgk.scoresense.config;

import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ConcurrentConfig {

    /**
     * 用于进行 IO 操作的线程池。
     */
    public final static ExecutorService CACHED_EXECUTORS = Executors.newCachedThreadPool();

    /**
     * 用于进行无 IO 操作的线程池。
     */
    public final static ExecutorService FIXED_EXECUTORS = Executors.newFixedThreadPool(40);

}
