package com.linlazy.chat.channel;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChatChannel {
    private Map<Integer,AbstractChatChannel> map = new HashMap();

    @PostConstruct
    public void init(){
        map.put(chatType(),this);
    }

    protected abstract int chatType();


    /**
     * 发送聊天信息
     * @param message
     */
    public abstract void sendChat(String message);
}
