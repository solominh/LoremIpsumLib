package com.lorem_ipsum.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hoangminh on 12/29/15.
 */
public class ThreadUtils {

    /**
     * public ThreadPoolExecutor(
     * int corePoolSize,
     * int maxPoolSize,
     * long keepAlive,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue,
     * RejectedExecutionHandler handler);
     */

    private static ThreadPoolExecutor mThreadPoolExecutor = null;

    public static ThreadPoolExecutor getPool() {
        if (mThreadPoolExecutor == null) {
            // Get processors
            int numberOfCores = Runtime.getRuntime().availableProcessors();
            if (numberOfCores < 1)
                numberOfCores = 1;

            // The amount of time an idle thread can last
            long keepAliveTime = 60 * 5; // 5 minutes

            // Create thread pool
            mThreadPoolExecutor = new ThreadPoolExecutor(
                    numberOfCores,
                    numberOfCores * 8,
                    keepAliveTime,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>()
            );

            // Normal: core thread never die
            mThreadPoolExecutor.allowCoreThreadTimeOut(true);
            // Start all core thread when executor is created
            mThreadPoolExecutor.prestartAllCoreThreads();
        }
        return mThreadPoolExecutor;
    }

    public static void shutdown() {
        if (mThreadPoolExecutor != null) {
            // Terminate all thread eventually (use shutdownNow to terminate all threads immediately)
            mThreadPoolExecutor.shutdown();
            mThreadPoolExecutor = null;
        }
    }
}
