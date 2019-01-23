package com.linlazy.mmorpg.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.dto.ChatDTO;
import com.linlazy.mmorpg.server.common.PushHelper;

import java.util.Set;

/**
 * 聊天推送类
 * @author linlazy
 */
public class ChatPushHelper {

    public static void pushChatContent(long actorId, Set<ChatDTO> chatDTO) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("chat", chatDTO);
        PushHelper.push(actorId, jsonObject);
    }

    public static void broadcastChatContent(long actorId,String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", message);
        PushHelper.broadcast(jsonObject);
    }

    public static void pushPrivateChatContent(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", message);
        PushHelper.push(actorId, jsonObject);
    }
}
