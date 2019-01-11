package com.linlazy.mmorpg.server.db.queue;


import com.linlazy.mmorpg.server.db.dao.BaseJdbc;
import com.linlazy.mmorpg.server.db.entity.Entity;

import java.util.Collection;

/**
 * @author linlazy
 */
public class DBTask implements Runnable{

    private final Collection<Entity> entities;
    private final BaseJdbc baseJdbc;

    public DBTask(Collection<Entity> entities, BaseJdbc baseJdbc) {
        this.entities = entities;
        this.baseJdbc = baseJdbc;
    }

    @Override
    public void run() {
             entities.stream().forEach(
                     entity -> {
                         switch (entity.getOperatorType()){

                             case EntityOperatorType.INSERT:
                                 baseJdbc.doInsert(entity);
                                 break;
                             case EntityOperatorType.UPDATE:
                                 baseJdbc.doUpdate(entity);
                                 break;
                             case EntityOperatorType.DELETE:
                                 baseJdbc.doDelete(entity);
                                 break;
                             default:
                         }
                     }
             );
    }

}
