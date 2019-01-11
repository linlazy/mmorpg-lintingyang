package com.linlazy.mmorpg.module.chat.service.channel;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class BaseChatChannel {

    private static Map<Integer, BaseChatChannel> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(chatType(),this);
    }

    /**
     * 聊天频道
     * @return 返回聊天频道（私聊，全服）
     */
    protected abstract int chatType();

    public static BaseChatChannel getChatChannel(int chatType){
        return map.get(chatType);
    }

    /**
     * 发送聊天信息
     * @param actorId 玩家ID
     * @param jsonObject 可变参数
     * @return 返回发送聊天结果
     */
    public abstract Result<?> sendChat(long actorId, JSONObject jsonObject);

}
