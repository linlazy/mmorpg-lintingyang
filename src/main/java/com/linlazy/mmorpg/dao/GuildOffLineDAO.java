package com.linlazy.mmorpg.dao;


import com.linlazy.mmorpg.entity.GuildOffLineEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

/**
 * 公会离线消息访问类
 * @author linlazy
 */
@Component
public class GuildOffLineDAO extends EntityDAO<GuildOffLineEntity> {


    @Override
    protected Class<GuildOffLineEntity> forClass() {
        return GuildOffLineEntity.class;
    }
}
