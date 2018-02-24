package com.cj.engine.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public final class ThreadPoolFactory {

    public static ExecutorService newThreadPool(String threadNameFormat, int corePoolSize, int maxPoolSize, long keepAliveTimeMilliseconds) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadNameFormat).build();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTimeMilliseconds, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), threadFactory, new ThreadPoolExecutor.AbortPolicy());

    }
}
