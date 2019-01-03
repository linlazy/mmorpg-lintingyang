package com.linlazy.mmorpglintingyang.module.guild.dao;

import com.linlazy.mmorpglintingyang.module.guild.entity.Guild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author linlazy
 */
@Mapper
public interface GuildDao {

    /**
     * 获取公会信息
     * @param guildId 公会ID
     * @return 返回公会信息
     */
    @Select("select * from guild where guildId = #{guildId}")
    Guild getGuild(long guildId);


    /**
     * 更新公会信息
     * @param guild 公会信息
     */
    @Update("update guild where guildId = #{guildId}")
    void updateGuild(Guild guild);


}
