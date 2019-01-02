package com.linlazy.mmorpglintingyang.module.guild.entity;

import lombok.Data;

@Data
public class Guild {

    /**
     * 公会ID
     */
    private long guildId;
    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 权限级别
     */
    private int authLevel;
}
