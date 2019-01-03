package com.linlazy.mmorpglintingyang.module.guild.dao;

import com.linlazy.mmorpglintingyang.module.guild.entity.GuildWarehouse;
import org.apache.ibatis.annotations.*;

import java.util.Set;

/**
 * 公会仓库访问类
 * @author linlazy
 */
@Mapper
public interface GuildWarehouseDao {

    /**
     * 获取公会仓库信息
     * @param guildId 公会ID
     * @return 返回公会仓库信息
     */
    @Select("select * from guild_warehouse where guidId = #{guildId}")
    Set<GuildWarehouse> getGuildWarehouse(long guildId);

    /**
     * 增加公会仓库信息
     * @param guildWarehouse 公会仓库信息
     */
    @Insert({"insert into guild_warehouse(guildId,itemId,count,ext) ",
            "values(#{guildId},#{itemId},#{count},#{ext})"})
    void addGuildWarehouse(GuildWarehouse guildWarehouse);


    /**
     * 清空公会仓库信息
     * @param guildId  公会ID
     */
    @Delete("delete from guild_warehouse where guildId = #{guildId}")
    void deleteGuildWarehouses(long guildId);

    /**
     * 更新公会仓库信息
     * @param guildWarehouse 公会仓库信息
     */
    @Update("update guild_warehouse set count = #{count},ext = #{ext} where guildId = #{guildId} and itemId = #{itemId}")
    void updateGuildWarehouse(GuildWarehouse guildWarehouse);


    /**
     * 删除公会仓库信息
     * @param guildWarehouse 公会仓库信息
     */
    @Delete("delete from guild_warehouse where guildId = #{guildId} and itemId = #{itemId}")
    void deleteGuildWarehouse(GuildWarehouse guildWarehouse);
}
