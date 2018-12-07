package com.linlazy.game.protocol.packet;


import lombok.Data;

@Data
public abstract class Packet {

    /**
     * 协议版本号
     */
    private byte protocolVersion = 1;

    public abstract byte getCommand();
}
