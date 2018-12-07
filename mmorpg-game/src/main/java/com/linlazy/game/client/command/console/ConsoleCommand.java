package com.linlazy.game.client.command.console;

import io.netty.channel.Channel;

import java.util.Scanner;

public interface ConsoleCommand {

    /**
     * 执行命令
     */
    void exec(Scanner scanner , Channel channel);
}
