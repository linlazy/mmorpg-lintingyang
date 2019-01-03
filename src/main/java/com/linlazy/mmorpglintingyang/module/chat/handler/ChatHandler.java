package com.linlazy.mmorpglintingyang.module.chat.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.chat.service.ChatService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 聊天
 * @author linlazy
 */
@Module("chat")
@Component
public class ChatHandler {

    @Autowired
    private ChatService chatService;

    /**
     * 发送聊天信息
     * @param jsonObject
     * @return
     */
    @Cmd("send")
    public Result<?> send(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return chatService.sendChat(actorId,jsonObject);
    }
}
