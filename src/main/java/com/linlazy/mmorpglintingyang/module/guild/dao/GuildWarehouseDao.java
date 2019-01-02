package com.linlazy.mmorpglintingyang.module.guild.dao;

import com.linlazy.mmorpglintingyang.module.guild.entity.GuildWarehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 公会仓库访问类
 */
@Mapper
public interface GuildWarehouseDao {

    @Select("select * from guild_warehouse where guidId = #{guildId}")
    GuildWarehouse getGuildWarehouse(long guildId);
}
