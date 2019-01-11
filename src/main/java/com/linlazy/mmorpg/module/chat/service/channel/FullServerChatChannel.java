package com.linlazy.mmorpg.module.chat.service.channel;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpg.module.chat.constants.ChatType;
import com.linlazy.mmorpg.module.chat.dao.ChatDao;
import com.linlazy.mmorpg.module.chat.dto.ChatDTO;
import com.linlazy.mmorpg.module.chat.push.ChatPushHelper;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 全服频道
 * @author linlazy
 */
@Component
public class FullServerChatChannel extends BaseChatChannel {

    @Override
    protected int chatType() {
        return 2;
    }


    @Autowired
    private ChatDao chatDao;

    @Override
    public Result<?> sendChat(long actorId, JSONObject jsonObject) {

        String content = jsonObject.getString("content");

        ChatDTO chatDTO = new ChatDTO();

        chatDTO.setSourceId(actorId);
        chatDTO.setContent(content);
        chatDTO.setChatType(ChatType.FULL_SERVER);
        ChatPushHelper.broadcastChatContent(actorId, Sets.newHashSet(chatDTO));
        return Result.success();

    }
}