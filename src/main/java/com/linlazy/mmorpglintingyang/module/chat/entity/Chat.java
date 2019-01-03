package com.linlazy.mmorpglintingyang.module.chat.entity;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class Chat {

    /**
     * 聊天标识
     */
    private long chatId;

    /**
     * 聊天类型
     */
    private int chatType;
    /**
     * 来源
     */
    private long sourceId;

    /**
     * 接受者
     */
    private long receiver;

    /**
     * 内容
     */
    private String content;



}
