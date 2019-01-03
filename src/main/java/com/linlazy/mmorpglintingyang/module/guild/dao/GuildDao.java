package com.linlazy.mmorpglintingyang.module.guild.dao;

import com.linlazy.mmorpglintingyang.module.guild.entity.Guild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GuildDao {

    @Select("select * from guild where guildId = #{guildId}")
    Guild getGuildGold(long guildId);


    @Update("update guild where guildId = #{guildId}")
    void updateGuildGold(Guild guild);


}
