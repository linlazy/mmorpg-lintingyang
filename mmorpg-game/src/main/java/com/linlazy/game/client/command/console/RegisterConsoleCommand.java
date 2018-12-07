package com.linlazy.game.client.command.console;

import com.linlazy.game.protocol.constants.RequestCommand;
import com.linlazy.game.protocol.packet.Packet;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.Scanner;

/**
 * 注册指令
 */
public class RegisterConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("正在注册用户...");
        RegisterRequestPacket registerRequestPacket = new RegisterRequestPacket();
        System.out.print("请输入用户名：");
        registerRequestPacket.setUsername(scanner.nextLine());
        System.out.print("请输入密码：");
        registerRequestPacket.setPassword(scanner.nextLine());
        channel.writeAndFlush(registerRequestPacket);
    }

    @Data
    private class RegisterRequestPacket extends Packet {
        private String username;
        private String password;

        @Override
        public byte getCommand() {
            return RequestCommand.REGISTER_REQUEST;
        }
    }
}
