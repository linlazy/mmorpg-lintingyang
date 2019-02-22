package com.linlazy.mmorpg.module.chat.dto;

import com.linlazy.mmorpg.module.chat.domain.Chat;
import com.linlazy.mmorpg.pb.PBModule1Chat;

/**
 *
 * @author linlazy
 */
public class PushChatDTO {

    private Chat chat;

    public PushChatDTO(Chat chat) {
        this.chat = chat;
    }

    public byte[] toMessage() {
        return PBModule1Chat.PushChat.newBuilder()
//                .setChatType(chat.getChatType())
//                .setContent(chat.getContent())
//                .setExt(JSON.toJSONString(chat.getExt()))
                .build().toByteArray();
    }
}
