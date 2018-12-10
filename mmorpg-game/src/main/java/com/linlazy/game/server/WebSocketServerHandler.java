package com.linlazy.game.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final String WEBSOCKET_PATH = "/websoket";

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest){
            //握手
            handleHttpRequest(channelHandlerContext,(FullHttpRequest)msg);
        }else {
            handleWebSocket(channelHandlerContext,(WebSocketFrame)msg);
        }
    }

    private void handleWebSocket(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) {

        if(webSocketFrame instanceof TextWebSocketFrame){
            String text = ((TextWebSocketFrame) webSocketFrame).text();
            JSONObject jsonObject = JSON.parseObject(text);
        }

    }

    private void handleHttpRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest httpRequest) {
        //组装路径
        String location = "ws://" + httpRequest.headers().get(HttpHeaderNames.HOST) + WEBSOCKET_PATH;
        //握手
        WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory(location,null,true);
        WebSocketServerHandshaker webSocketServerHandshaker = webSocketServerHandshakerFactory.newHandshaker(httpRequest);
        webSocketServerHandshaker.handshake(channelHandlerContext.channel(),httpRequest);
    }
}