package com.linlazy.game.client.repsonse;

import com.linlazy.game.protocol.constants.ResponseCommand;
import com.linlazy.game.protocol.packet.ResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponseHandler.LoginResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponsePacket loginResponsePacket) throws Exception {

        if(loginResponsePacket.isSuccess()){
            System.out.println("登录成功！");
        }else {
            System.out.println(String.format("登录失败【%s】",loginResponsePacket.getReason()));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端挂关闭");
        super.channelInactive(ctx);
    }

    @Data
    public class LoginResponsePacket extends ResponsePacket {
        @Override
        public byte getCommand() {
            return ResponseCommand.LOGIN_RESPONSE;
        }
    }
}
