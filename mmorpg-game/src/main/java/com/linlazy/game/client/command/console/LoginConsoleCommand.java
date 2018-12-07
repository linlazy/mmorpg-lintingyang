package com.linlazy.game.client.command.console;

import com.linlazy.game.client.request.LoginRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 登录指令
 */
public class LoginConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("正在登录...");
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        System.out.print("请输入用户名：");
        loginRequestPacket.setUsername(scanner.nextLine());
        System.out.print("请输入密码：");
        loginRequestPacket.setPassword(scanner.nextLine());
        channel.writeAndFlush(loginRequestPacket);
    }

}
