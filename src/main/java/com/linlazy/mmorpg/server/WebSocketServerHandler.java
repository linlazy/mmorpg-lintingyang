package com.linlazy.mmorpg.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.LogoutListener;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.route.GameRouter;
import com.linlazy.mmorpg.utils.SessionManager;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

/**
 * @author linlazy
 */
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

            JSONObject jsonObjectRequest = JSON.parseObject(text);
            jsonObjectRequest.put("channel",channelHandlerContext.channel());
            Result<?> result = GameRouter.handleRoute(jsonObjectRequest);
            if(result == null){
                throw new RuntimeException(String.format("not match !, module[%s],cmd[%s]",jsonObjectRequest.getString("module"),jsonObjectRequest.getString("command")));
            }
            JSONObject jsonObjectResponse = new JSONObject();
            jsonObjectResponse.put("module",jsonObjectRequest.getString("module"));
            jsonObjectResponse.put("command",jsonObjectRequest.getString("command"));
            jsonObjectResponse.put("code",result.getCode());
            jsonObjectResponse.put("data",result.getData());

            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(jsonObjectResponse.toJSONString());
            channelHandlerContext.writeAndFlush(textWebSocketFrame);
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


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        long actorId = SessionManager.getActorId(ctx.channel());

        SpringContextUtil.getApplicationContext().getBeansOfType(LogoutListener.class).values()
                .stream().forEach(logoutListener -> logoutListener.logout(actorId));

        SessionManager.unBind(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
