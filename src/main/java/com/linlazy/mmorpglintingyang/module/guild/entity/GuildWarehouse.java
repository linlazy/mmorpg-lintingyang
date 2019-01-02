package com.linlazy.mmorpglintingyang.module.guild.entity;

import lombok.Data;

/**
 * 公会仓库
 */
@Data
public class GuildWarehouse {

    /**
     * 公会ID
     */
    private long guildId;

    /**
     * 金币
     */
    private int gold;
    /**
     * 道具ID
     */
    private long itemId;

    /**
     * 数量
     */
    private int count;

    /**
     * 扩展属性
     */
    private String ext;
}
