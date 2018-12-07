package com.linlazy.game.protocol.packet;

import lombok.Data;

@Data
public abstract class ResponsePacket extends Packet{

    protected boolean success;

    protected String reason;
}
