package com.linlazy.mmorpglintingyang.module.chat.dto;

import com.linlazy.mmorpglintingyang.module.chat.entity.Chat;
import lombok.Data;

/**
 * @author linlazy
 */
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

    /**
     * 聊天类型
     */
    private int chatType;

    public ChatDTO(Chat chat) {
        this.sourceId = chat.getSourceId();
        this.content = chat.getContent();
    }

    public ChatDTO() {

    }
}
