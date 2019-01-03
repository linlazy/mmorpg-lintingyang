package com.linlazy.mmorpglintingyang.module.chat.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.chat.service.channel.ChatChannel;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class ChatService {
    public Result<?> sendChat(long actorId, JSONObject jsonObject) {
        int chatType = jsonObject.getIntValue("chatType");
        ChatChannel chatChannel = ChatChannel.getChatChannel(chatType);
        return chatChannel.sendChat(actorId,jsonObject);
    }
}
