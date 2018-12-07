package com.linlazy.game.client.command.console;

import com.linlazy.game.protocol.constants.RequestCommand;
import com.linlazy.game.protocol.packet.Packet;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.Scanner;

/**
 * 登录指令
 */
public class LogoutConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        LogoutRequestPacket logoutRequestPacket = new LogoutRequestPacket();
        channel.writeAndFlush(logoutRequestPacket);
    }

    @Data
    private class LogoutRequestPacket extends Packet {
        @Override
        public byte getCommand() {
            return RequestCommand.LOGOUT_REQUEST;
        }
    }
}
