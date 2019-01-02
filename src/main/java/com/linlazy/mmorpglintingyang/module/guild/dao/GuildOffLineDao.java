package com.linlazy.mmorpglintingyang.module.guild.dao;


import com.linlazy.mmorpglintingyang.module.guild.entity.GuildOffLine;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GuildOffLineDao {

    @Insert({"insert into guild_offline (guildId,receiver,sourceId,type)",
    "values (#{guildId},#{receiver},#{sourceId},#{type})"})
    void addGuildOffLine(GuildOffLine guildOffLine);

}
