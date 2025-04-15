package com.jasper.core.contractor.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

@Slf4j
@Component
public class ForkJoinUtils {

    private final ForkJoinPool forkJoinPool;

    public ForkJoinUtils() {
        forkJoinPool = new ForkJoinPool();
    }

    public void destroy() {
        forkJoinPool.shutdown();
        log.info("ForkJoinUtils destroyed");
    }

    public <T> T execute(RecursiveTask<T> task) {

        Future<T> future = forkJoinPool.submit(task);
        T result;
        try {
            result = future.get();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(interruptedException);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}