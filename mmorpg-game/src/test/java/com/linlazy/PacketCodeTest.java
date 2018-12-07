package com.linlazy;

import com.linlazy.game.client.command.console.LoginConsoleCommand;
import com.linlazy.game.client.request.LoginRequestPacket;
import com.linlazy.game.protocol.packet.Packet;
import com.linlazy.game.protocol.packet.PacketCodeC;
import com.linlazy.game.protocol.packet.serialize.Serializer;
import com.linlazy.game.protocol.packet.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Assert;
import org.junit.Test;

public class PacketCodeTest {

    @Test
    public void test(){
        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setUsername("zhangsan");
        loginRequestPacket.setPassword("password");

        PacketCodeC packetCodeC = PacketCodeC.INSTANCE;
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();

        packetCodeC.encode(byteBuf, loginRequestPacket);
        Packet decodedPacket = packetCodeC.decode(byteBuf);

        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));
    }
}
