package com.linlazy.mmorpg.server.threadpool;

import com.linlazy.mmorpg.server.common.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 线程池
 * @author linlazy
 */
public class ThreadOrderPool {


    public static Map<String,ThreadOrderPool> threadOrderPoolMap = new ConcurrentHashMap<>();


    private AtomicInteger index  = new AtomicInteger(0);


    private final int nThreads;

    private static final Map<Long, Integer> map = new ConcurrentHashMap<>();


    private List<ExecutorService> executorServices;

    /**
     * 线程数
     * @param nThreads
     */
    public ThreadOrderPool(int nThreads) {
        this.index = index;
        this.nThreads = nThreads;
        executorServices = new ArrayList<>( this.nThreads);
        for(int i = 0 ; i < this.nThreads ; i ++){
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorServices.add(executorService);
        }

    }

    public Future<Result<?>> execute(BusinessTask businessTask){
        Integer index = map.get(businessTask.getIdentity());
        if(index == null){
            index = this.index.getAndIncrement()%nThreads;
            map.put(businessTask.getIdentity(),index);
        }
        return executorServices.get(index).submit(businessTask);
    }
}
