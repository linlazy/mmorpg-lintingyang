package com.linlazy.game.common;


import io.netty.channel.Channel;
import io.netty.util.Attribute;

import java.util.HashMap;
import java.util.Map;

public class SessionUtil {

    private Map<Long,Channel> actorIdChannelMap = new HashMap<>();
    private Map<Channel,Long> channelActorIdMap = new HashMap<>();

    /**
     * 是否已经登录
     *
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel) {
        return false;
    }

    public static void unBindSession(Channel channel) {
        Attribute<Session> attr = channel.attr(ChannelAttributeKey.SESSION);
        if(attr != null){
            attr.remove();
        }
    }
}