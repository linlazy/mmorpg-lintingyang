package com.linlazy.game.client.command.console;

import com.linlazy.game.protocol.constants.RequestCommand;
import com.linlazy.game.protocol.packet.Packet;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.Scanner;

/**
 * 移动场景指令
 */
public class MoveSceneConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        MoveSceneRequestPacket moveSceneRequestPacket = new MoveSceneRequestPacket();

        System.out.print("【移动场景】，请输入场景ID：");
        int targetSceneId = scanner.nextInt();

        moveSceneRequestPacket.setTargetSceneId(targetSceneId);
        channel.writeAndFlush(moveSceneRequestPacket);
    }

    @Data
    private class MoveSceneRequestPacket extends Packet {
        private int targetSceneId;

        @Override
        public byte getCommand() {
            return RequestCommand.MOVE_SCENE_REQUEST;
        }
    }
}
