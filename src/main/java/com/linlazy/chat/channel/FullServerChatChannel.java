package com.linlazy.chat.channel;

/**
 * 私聊频道
 */
public class FullServerChatChannel extends AbstractChatChannel{


    @Override
    protected int chatType() {
        return 2;
    }

    @Override
    public void sendChat(String message) {

    }
}
