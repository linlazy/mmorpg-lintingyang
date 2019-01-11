package com.linlazy.mmorpg;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author linlazy
 */

public class GuavaCacheTest {

    public static void main(String[] args) throws InterruptedException {
        LoadingCache<String, Integer> cache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .recordStats()
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return -1;
                    }
                });

        System.out.println(cache.getIfPresent("key1"));
        cache.put("key1",1);
        System.out.println(cache.getIfPresent("key1"));

        try {
            System.out.println("key2:" + cache.get("key2"));

            cache.put("key2",2);
            System.out.println("key2:" + cache.get("key2"));

            for(int i = 3; i < 13; i ++){
                cache.put("key"+i,i);
            }

            System.out.println("size:"+ cache.size());

            System.out.println("key2:" + cache.get("key2"));

            Thread.sleep(5000);
            System.out.println("5秒后...");
            cache.put("key1",1);
            cache.put("key2",2);

            Thread.sleep(5000);
            System.out.println("5秒后...");
            System.out.println("size:" + cache.size());

            System.out.println("key1:" + cache.getIfPresent("key1"));
            System.out.println("size :" + cache.size());

        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
