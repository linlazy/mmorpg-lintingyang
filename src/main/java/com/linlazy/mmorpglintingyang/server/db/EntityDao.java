package com.linlazy.mmorpglintingyang.server.db;

import com.linlazy.mmorpglintingyang.server.db.cache.DBCache;

import java.util.Queue;
import java.util.concurrent.ExecutionException;

/**
 * @author linlazy
 */
public class EntityDao<T extends Entity> {

    private DBCache dbCache;

    /**
     * 实体队列
     */
    protected Queue<T> queue;

    /**
     * 更新队列
     * @param entity
     */
    public void updateQueue(T entity){

//        //更新缓存
//        dbCache.putEntity(entity.getIdentity(),entity);
//        //放进队列
//        queue.add(entity);
    }

    /**
     * 获取实体
     * @param identity
     * @return
     * @throws ExecutionException
     */
    public Entity getEntity(Identity identity) throws ExecutionException {
        return dbCache.getEntity(identity);
    }
}
