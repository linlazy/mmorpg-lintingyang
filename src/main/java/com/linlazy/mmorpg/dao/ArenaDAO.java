package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.ArenaEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class ArenaDAO extends EntityDAO<ArenaEntity> {

    @Override
    protected Class<ArenaEntity> forClass() {
        return ArenaEntity.class;
    }
}
