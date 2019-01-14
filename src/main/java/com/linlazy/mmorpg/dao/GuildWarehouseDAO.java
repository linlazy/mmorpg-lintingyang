package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.GuildWarehouseEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 公会仓库访问类
 * @author linlazy
 */
@Component
public class GuildWarehouseDAO extends EntityDAO<GuildWarehouseEntity> {

    /**
     * 获取公会仓库信息
     * @param guildId 公会ID
     * @return 返回公会仓库信息
     */
    public List<GuildWarehouseEntity> getGuildWarehouseEntity(long guildId){
        return jdbcTemplate.queryForList("select * from guild_warehouse where guidId = ?",new Object[]{guildId},GuildWarehouseEntity.class);
    }

    /**
     * 清空公会仓库信息
     * @param guildId  公会ID
     */
    int deleteGuildWarehousesEntity(long guildId){
        return jdbcTemplate.update("delete from guild_warehouse where guildId = ?", guildId);
    }


    @Override
    protected Class<GuildWarehouseEntity> forClass() {
        return GuildWarehouseEntity.class;
    }
}
