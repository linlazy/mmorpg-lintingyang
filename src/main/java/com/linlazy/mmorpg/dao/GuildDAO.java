package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.GuildEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

/**
 * 公会访问类
 * @author linlazy
 */
@Component
public class GuildDAO extends EntityDAO<GuildEntity> {

    @Override
    protected Class<GuildEntity> forClass() {
        return GuildEntity.class;
    }
}
