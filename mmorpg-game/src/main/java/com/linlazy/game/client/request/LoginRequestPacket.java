package com.linlazy.game.client.request;

import com.linlazy.game.protocol.constants.RequestCommand;
import com.linlazy.game.protocol.packet.Packet;
import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {
    private String username;
    private String password;

    @Override
    public byte getCommand() {
        return RequestCommand.LOGIN_REQUEST;
    }
}