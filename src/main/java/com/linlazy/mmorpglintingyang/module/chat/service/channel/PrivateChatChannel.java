package com.linlazy.mmorpglintingyang.module.chat.service.channel;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.chat.dto.ChatDTO;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.SessionManager;

public class PrivateChatChannel extends ChatChannel{
    @Override
    protected int chatType() {
        return 1;
    }

    @Override
    public Result<?> sendChat(long actorId, JSONObject jsonObject) {
        ChatDTO chatDTO = new ChatDTO();

        long targetId = jsonObject.getLongValue("targetId");
        String content = jsonObject.getString("content");
        //玩家在线
        if(SessionManager.isOnline(targetId)){
            chatDTO.setSourceId(actorId);
            chatDTO.setContent(content);
            return Result.success(chatDTO);
        }else {
            return Result.success(chatDTO);
        }
    }
}
