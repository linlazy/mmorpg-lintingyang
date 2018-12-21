package com.linlazy.mmorpglintingyang.module.chat.dto;

import lombok.Data;

@Data
public class ChatDTO {

    /**
     *  内容来源
     */
    long sourceId;

    /**
     * 聊天内容
     */
    private String content;
}
