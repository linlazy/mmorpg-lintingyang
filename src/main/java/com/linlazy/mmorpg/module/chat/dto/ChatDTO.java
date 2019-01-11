package com.linlazy.mmorpg.module.chat.dto;

import com.linlazy.mmorpg.module.chat.entity.Chat;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class ChatDTO {

    /**
     *  内容来源
     */
    Long sourceId;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 聊天类型
     */
    private Integer chatType;

    public ChatDTO(Chat chat) {
        this.sourceId = chat.getSourceId();
        this.content = chat.getContent();
    }

    public ChatDTO() {

    }
}
