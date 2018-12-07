package com.linlazy.game.client.command.console;

import io.netty.channel.Channel;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleCommandManager implements ConsoleCommand {
    private Map<String,ConsoleCommand> consoleCommandMap = new HashMap();

    public ConsoleCommandManager() {
        consoleCommandMap.put("moveScene",new MoveSceneConsoleCommand());
        consoleCommandMap.put("logout",new LogoutConsoleCommand());
        consoleCommandMap.put("register",new RegisterConsoleCommand());
    }

    public void exec(Scanner scanner, Channel channel){

        String command = scanner.nextLine();
        ConsoleCommand consoleCommand = consoleCommandMap.get(command);
        if(consoleCommand == null){
            System.out.println(String.format("无法识别【】指令 ，请重新输入",command));
            return;
        }
        consoleCommand.exec(scanner,channel);
    }
}
