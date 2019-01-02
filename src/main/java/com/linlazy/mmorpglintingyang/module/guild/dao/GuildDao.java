package com.linlazy.mmorpglintingyang.module.guild.dao;

import com.linlazy.mmorpglintingyang.module.guild.entity.Guild;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface GuildDao {


    @Insert({"insert into guild(guildId,actorId,authLevel)"
    ,"values(#{guildId},#{actorId},#{authLevel})"})
    void addGuild(Guild guild);

    @Select("select * from guild where actorId = #{actorId} and guildId = #{guildId}")
    Guild getGuild(long guildId,long actorId);

    @Select("select * from guild where guildId = #{guildId}")
    Set<Guild> getGuildSet(long guildId);

    @Update({"update guild set authLevel = #{authLevel}",
            "where guildId = #{guildId} and actorId = #{actorId}"})
    Set<Guild> updateGuild(Guild guild);

    @Delete({"delete from guild",
            "where guildId = #{guildId} and actorId = #{actorId}"})
    void deleteGuild(Guild guild);
}
