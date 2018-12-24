package com.linlazy.mmorpglintingyang.module.chat.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.chat.dto.ChatDTO;
import com.linlazy.mmorpglintingyang.server.common.PushHelper;

import java.util.Set;

/**
 * 聊天推送类
 */
public class ChatPushHelper {

    public static void pushChatContent(long actorId, Set<ChatDTO> chatDTO) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("chat", chatDTO);
        PushHelper.push(actorId, jsonObject);
    }

    public static void broadcastChatContent(long actorId, Set<ChatDTO> chatDTO) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("chat", chatDTO);
        PushHelper.broadcast(jsonObject);
    }
}
