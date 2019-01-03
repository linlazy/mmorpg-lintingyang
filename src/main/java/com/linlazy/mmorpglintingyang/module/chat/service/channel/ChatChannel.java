package com.linlazy.mmorpglintingyang.module.chat.service.channel;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class ChatChannel {

    private static Map<Integer,ChatChannel> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(chatType(),this);
    }

    protected abstract int chatType();

    public static ChatChannel getChatChannel(int chatType){
        return map.get(chatType);
    }

    public abstract Result<?> sendChat(long actorId, JSONObject jsonObject);

}
