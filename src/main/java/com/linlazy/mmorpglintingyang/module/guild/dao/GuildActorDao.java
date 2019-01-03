package com.linlazy.mmorpglintingyang.module.guild.dao;

import com.linlazy.mmorpglintingyang.module.guild.entity.GuildActor;
import org.apache.ibatis.annotations.*;

import java.util.Set;

/**
 * @author linlazy
 */
@Mapper
public interface GuildActorDao {


    @Insert({"insert into guild_actor(guildId,actorId,authLevel)"
    ,"values(#{guildId},#{actorId},#{authLevel})"})
    void addGuild(GuildActor guildActor);

    @Select("select * from guild_actor where actorId = #{actorId} and guildId = #{guildId}")
    GuildActor getGuild(long guildId, long actorId);

    @Select("select * from guild_actor where guildId = #{guildId}")
    Set<GuildActor> getGuildSet(long guildId);

    @Update({"update guild_actor set authLevel = #{authLevel}",
            "where guildId = #{guildId} and actorId = #{actorId}"})
    Set<GuildActor> updateGuild(GuildActor guildActor);

    @Delete({"delete from guild_actor",
            "where guildId = #{guildId} and actorId = #{actorId}"})
    void deleteGuild(GuildActor guildActor);
}
