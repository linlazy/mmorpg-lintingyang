package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.TaskEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public  class TaskDAO extends EntityDAO<TaskEntity> {

    @Override
    protected Class<TaskEntity> forClass() {
        return TaskEntity.class;
    }
}
