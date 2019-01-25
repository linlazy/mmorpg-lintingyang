package com.linlazy.mmorpg.server.db.dao;

import com.linlazy.mmorpg.server.db.entity.Entity;
import com.linlazy.mmorpg.server.db.entity.EntityInfo;
import com.linlazy.mmorpg.server.db.queue.EntityOperatorType;

/**
 * @author linlazy
 */
public abstract class EntityDAO<T extends Entity> extends BaseJdbc<T>{

    /**
     * 制定操作的实体类类型
     * @return
     */
    protected abstract Class<T> forClass();

    /**
     * 更新
     * @param entity
     */
    public void updateQueue(T entity){
        entity.setOperatorType(EntityOperatorType.UPDATE);
        entity.beforeWriteDB();
        entity.init();
        //放进队列
        dbQueueManager.pushEntity(entity);
    }

    /**
     * 插入
     * @param entity
     */
    public void insertQueue(T entity){
        entity.setOperatorType(EntityOperatorType.INSERT);
        entity.beforeWriteDB();
        entity.init();
        //放进队列
        dbQueueManager.pushEntity(entity);
    }

    /**
     * 删除
     * @param entity
     */
    public void deleteQueue(T entity){
        entity.setOperatorType(EntityOperatorType.DELETE);
        entity.beforeWriteDB();
        entity.init();
        //放进队列
        dbQueueManager.pushEntity(entity);
    }

    /**
     * 获取实体
     * @return
     */
    public T getEntityByPK(Object...  pkArgs){
        EntityInfo entityInfo = EntityInfo.ENTITY_INFO_MAP.get(forClass());
        return this.queryByPK(entityInfo,pkArgs);
    }

    public T queryEntity(String selectPrepareSQL,Object... args){
        EntityInfo entityInfo = EntityInfo.ENTITY_INFO_MAP.get(forClass());
        return this.query(entityInfo,selectPrepareSQL,args);
    }

}
