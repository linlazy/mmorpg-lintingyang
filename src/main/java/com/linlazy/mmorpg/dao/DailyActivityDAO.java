package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.DailyActivityEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

/**
 * 日常活动访问类
 * @author linlazy
 */
@Component
public class DailyActivityDAO extends EntityDAO<DailyActivityEntity> {

    @Override
    protected Class<DailyActivityEntity> forClass() {
        return DailyActivityEntity.class;
    }
}
