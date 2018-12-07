package com.linlazy.chat.channel;

/**
 * 私聊频道
 */
public class PrivateChatChannel extends AbstractChatChannel{


    @Override
    protected int chatType() {
        return 1;
    }

    @Override
    public void sendChat(String message) {

    }
}
