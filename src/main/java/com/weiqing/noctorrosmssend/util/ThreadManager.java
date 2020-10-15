package com.weiqing.noctorrosmssend.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author caoyidi
 * @date 19-6-4 上午10:20
 */
@Slf4j
public class ThreadManager {
    public static Lock lock = new ReentrantLock(true);
    /**
     * 通过ThreadPoolExecutor的代理类来对线程池的管理
     */
    private static ThreadPoolProxy mThreadPoolProxy;

    public static ThreadPoolExecutor generateThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                                             String name) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                //核心线程数量
                corePoolSize,
                //最大线程数量
                maximumPoolSize,
                //当线程空闲时，保持活跃的时间
                keepAliveTime,
                //时间单元 ，毫秒级
                TimeUnit.MILLISECONDS,
                //线程任务队列
                new LinkedBlockingQueue<>(),
                //创建线程的工厂
                new NamedThreadFactory(name));
        //堵塞任务等待执行
        threadPool.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor executor) -> {
                    if (!executor.isShutdown()) {
                        try {
                            executor.getQueue().put(r);
                        } catch (InterruptedException e) {
                            log.error(e.toString(), e);
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        return threadPool;
    }

    /**
     * 单列对象
     */
    public static ThreadPoolProxy getThreadPoolProxy() {
        synchronized (ThreadPoolProxy.class) {
            if (mThreadPoolProxy == null) {
                mThreadPoolProxy = new ThreadPoolProxy(3, 12, 1000);
            }
        }
        return mThreadPoolProxy;
    }

    /**
     * 通过ThreadPoolExecutor的代理类来对线程池的管理
     */
    public static class ThreadPoolProxy {
        private final int corePoolSize;
        private final long keepAliveTime;
        private final int maximumPoolSize;
        private String name;
        private ThreadPoolExecutor poolExecutor;//线程池执行者 ，java内部通过该api实现对线程池管理

        private ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        /**
         * 对外提供一个执行任务的方法
         *
         * @param r
         */
        public void execute(Runnable r) {
            if (poolExecutor == null || poolExecutor.isShutdown()) {
                poolExecutor = new ThreadPoolExecutor(
                        //核心线程数量
                        corePoolSize,
                        //最大线程数量
                        maximumPoolSize,
                        //当线程空闲时，保持活跃的时间
                        keepAliveTime,
                        //时间单元 ，毫秒级
                        TimeUnit.MILLISECONDS,
                        //线程任务队列
                        new LinkedBlockingQueue<>(),
                        //创建线程的工厂
                        new NamedThreadFactory("ThreadManager"));
            }
            poolExecutor.execute(r);
        }
    }
}

class NamedThreadFactory implements ThreadFactory {
    /**
     * 原子操作保证每个线程都有唯一的
     */
    private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);

    private final AtomicInteger M_THREAD_NUMBER = new AtomicInteger(1);
    private final boolean demoThread;
    private final String prefix;
    private final ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("default-thread-pool-" + THREAD_NUMBER.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix, boolean demo) {
        this.prefix = (!prefix.isEmpty()) ? prefix + "-thread-" : "";
        demoThread = demo;
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }


    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        String name = prefix + M_THREAD_NUMBER.getAndIncrement();
        Thread ret = new Thread(threadGroup, runnable, name, 0);
        ret.setDaemon(demoThread);
        return ret;
    }
}


