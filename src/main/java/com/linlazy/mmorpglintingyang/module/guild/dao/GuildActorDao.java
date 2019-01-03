package com.linlazy.mmorpglintingyang.module.guild.dao;

import com.linlazy.mmorpglintingyang.module.guild.entity.GuildActor;
import org.apache.ibatis.annotations.*;

import java.util.Set;

/**
 * @author linlazy
 */
@Mapper
public interface GuildActorDao {


    /**
     * 增加公会玩家
     * @param guildActor 公会玩家信息
     */
    @Insert({"insert into guild_actor(guildId,actorId,authLevel)"
    ,"values(#{guildId},#{actorId},#{authLevel})"})
    void addGuildActor(GuildActor guildActor);

    /**
     * 获取公会玩家信息
     * @param guildId  公会ID
     * @param actorId 玩家ID
     * @return 返回公会玩家信息
     */
    @Select("select * from guild_actor where actorId = #{actorId} and guildId = #{guildId}")
    GuildActor getGuildActor(long guildId, long actorId);


    /**
     * 获取公会所有玩家信息
     * @param guildId  公会ID
     * @return 返回公会所有玩家信息
     */
    @Select("select * from guild_actor where guildId = #{guildId}")
    Set<GuildActor> getGuildActorSet(long guildId);

    /**
     * 更新公会玩家信息
     * @param guildActor 公会玩家信息
     * @return
     */
    @Update({"update guild_actor set authLevel = #{authLevel}",
            "where guildId = #{guildId} and actorId = #{actorId}"})
    void updateGuildActor(GuildActor guildActor);

    /**
     * 删除公会玩家
     * @param guildActor 公会玩家信息
     */
    @Delete({"delete from guild_actor",
            "where guildId = #{guildId} and actorId = #{actorId}"})
    void deleteGuildActor(GuildActor guildActor);
}
