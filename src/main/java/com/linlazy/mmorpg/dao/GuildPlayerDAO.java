package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.GuildPlayerEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linlazy
 */
@Component
public class GuildPlayerDAO extends EntityDAO<GuildPlayerEntity> {

    /**
     * 获取公会所有玩家信息
     * @param guildId  公会ID
     * @return 返回公会所有玩家信息
     */
    List<GuildPlayerEntity> getGuildPlayerList(long guildId){
        return jdbcTemplate.queryForList("select * from guild_actor where guildId = ?",new Object[]{guildId},GuildPlayerEntity.class);
    }

    @Override
    protected Class<GuildPlayerEntity> forClass() {
        return GuildPlayerEntity.class;
    }
}
