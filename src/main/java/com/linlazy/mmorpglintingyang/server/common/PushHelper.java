package com.linlazy.mmorpglintingyang.server.common;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class PushHelper {

    public static void push(long actorId,JSONObject jsonObjectResponse){
        Channel channel = SessionManager.getChannel(actorId);

        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(jsonObjectResponse.toJSONString());
        channel.writeAndFlush(textWebSocketFrame);
    }
}
