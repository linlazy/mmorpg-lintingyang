package com.linlazy.mmorpglintingyang.module.guild.entity;

import lombok.Data;

@Data
public class Guild {
    /**
     * 公会ID
     */
    private long guildId;

    /**
     * 金币
     */
    private int gold;

    /**
     * 公会等级
     */
    private int level;
}
