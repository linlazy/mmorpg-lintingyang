package com.linlazy.game.client.repsonse;

import com.linlazy.game.common.SessionUtil;
import com.linlazy.game.protocol.constants.ResponseCommand;
import com.linlazy.game.protocol.packet.ResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponseHandler.LogoutResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LogoutResponsePacket logoutResponsePacket) throws Exception {
        //取消会话状态
        SessionUtil.unBindSession(channelHandlerContext.channel());
    }

    @Data
    public class LogoutResponsePacket extends ResponsePacket {
        @Override
        public byte getCommand() {
            return ResponseCommand.LOGOUT_RESPONSE;
        }
    }
}
