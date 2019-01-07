package com.linlazy.mmorpglintingyang.server.db.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpglintingyang.server.db.Entity;
import com.linlazy.mmorpglintingyang.server.db.Identity;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author linlazy
 */
@Component
public class DBCache<T extends Entity> {

    /**
     * 缓存大小
     */
    private int maximumSize;
    /**
     * 过期时间
     */
    private int expireTimesSeconds;

    /**
     * 缓存
     */
    private LoadingCache<Identity, T> cache;


    public  DBCache(){
        cache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireTimesSeconds, TimeUnit.SECONDS)
                .recordStats()
                .build(new CacheLoader<Identity,T>() {
                    @Override
                    public T load(Identity key) throws Exception {
                        return createEntity(key);
                    }
                });
    }

    public T getEntity(Identity identity) throws ExecutionException {
        return cache.get(identity);
    }

    private T createEntity(Identity key) {
        return null;
    }

    public  void putEntity(Identity identity, T entity) {
        cache.put(identity,entity);
    }
}
