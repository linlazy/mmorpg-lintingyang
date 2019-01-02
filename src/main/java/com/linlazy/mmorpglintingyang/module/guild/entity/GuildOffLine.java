package com.linlazy.mmorpglintingyang.module.guild.entity;

import lombok.Data;

@Data
public class GuildOffLine {

    /**
     * 公会Id
     */
    private long guild;

    /**
     * 消息接受者
     */
    private long receiver;
    /**
     * 消息来源
     */
    private long sourceId;
    /**
     * 消息类型
     */
    private int type;
}
