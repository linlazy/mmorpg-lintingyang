package com.linlazy.mmorpglintingyang.module.guild.dao;

import com.linlazy.mmorpglintingyang.module.guild.entity.GuildWarehouse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Set;

/**
 * 公会仓库访问类
 * @author linlazy
 */
@Mapper
public interface GuildWarehouseDao {

    @Select("select * from guild_warehouse where guidId = #{guildId}")
    Set<GuildWarehouse> getGuildWarehouse(long guildId);

    void addGuildWarehouse(GuildWarehouse guildWarehouse);

    @Delete("delete from guild_warehouse where guildId = #{guildId}")
    void deleteGuildWarehouses(long guildId);

    @Update("update guild_warehouse set count = #{count},ext = #{ext} where guildId = #{guildId} and itemId = #{itemId}")
    void updateGuildWarehouse(GuildWarehouse convertGuildWarehouse);

    @Delete("delete from guild_warehouse where guildId = #{guildId} and itemId = #{itemId}")
    void deleteGuildWarehouse(GuildWarehouse convertGuildWarehouse);
}
