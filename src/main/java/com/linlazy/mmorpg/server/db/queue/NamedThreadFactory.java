package com.linlazy.mmorpg.server.db.queue;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author linlazy
 */
public class NamedThreadFactory implements ThreadFactory {

    /**
     * 线程组
     */
    private ThreadGroup threadGroup;

    /**
     * 线程编号
     */
    private AtomicInteger threadNumber = new AtomicInteger();

    /**
     * 名字前置
     */
    private String namePrefix;

    public NamedThreadFactory(ThreadGroup threadGroup, String name) {
        this.threadGroup = threadGroup;
        this.namePrefix = threadGroup.getName() +"-"+name;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(threadGroup,r,this.namePrefix +"-"+ this.threadNumber.incrementAndGet());
    }
}
