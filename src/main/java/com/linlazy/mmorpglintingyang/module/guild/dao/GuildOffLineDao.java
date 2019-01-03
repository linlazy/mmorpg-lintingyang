package com.linlazy.mmorpglintingyang.module.guild.dao;


import com.linlazy.mmorpglintingyang.module.guild.entity.GuildOffLine;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author linlazy
 */
@Mapper
public interface GuildOffLineDao {

    /**
     * 增加公会离线消息
     * @param guildOffLine  公会离线消息
     */
    @Insert({"insert into guild_offline (guildId,receiver,sourceId,type)",
    "values (#{guildId},#{receiver},#{sourceId},#{type})"})
    void addGuildOffLine(GuildOffLine guildOffLine);

}
