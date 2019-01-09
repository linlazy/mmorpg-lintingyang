package com.linlazy.mmorpglintingyang.server.db;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author linlazy
 */
public abstract class EntityDao<T extends Entity> extends BaseJdbc<T>{

    protected abstract Class<T> forClass();


    /**
     * 实体队列
     */
    protected Queue<T> queue = new ConcurrentLinkedQueue<>();

    /**
     * 更新队列
     * @param entity
     */
    public void updateQueue(T entity){
        entity.beforeWriteDB();
//        //更新缓存
//        dbCache.putEntity(entity.getIdentity(),entity);
//        //放进队列
        queue.add(entity);
    }

    /**
     * 获取实体
     * @param identity
     * @return
     */
    public T getEntity(Identity identity){
        EntityInfo entityInfo = EntityInfo.ENTITY_INFO_MAP.get(forClass());
        return this.query(entityInfo,identity);
    }


}
