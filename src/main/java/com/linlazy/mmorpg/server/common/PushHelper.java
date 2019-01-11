package com.linlazy.mmorpg.server.common;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.utils.SessionManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Set;

/**
 * @author linlazy
 */
public class PushHelper {

    public static void push(long actorId,JSONObject jsonObjectResponse){
        Channel channel = SessionManager.getChannel(actorId);

        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(jsonObjectResponse.toJSONString());
        channel.writeAndFlush(textWebSocketFrame);
    }

    public static void broadcast(JSONObject jsonObjectResponse){
        Set<Channel> onlineChannelSet = SessionManager.getOnlineChannelSet();
        onlineChannelSet.stream()
                .forEach(channel -> {
                    TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(jsonObjectResponse.toJSONString());
                    channel.writeAndFlush(textWebSocketFrame);
                });
    }
}
