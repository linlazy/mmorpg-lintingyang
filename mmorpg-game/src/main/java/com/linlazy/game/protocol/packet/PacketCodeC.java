package com.linlazy.game.protocol.packet;

import com.linlazy.game.client.request.LoginRequestPacket;
import com.linlazy.game.protocol.constants.RequestCommand;
import com.linlazy.game.protocol.packet.serialize.Serializer;
import com.linlazy.game.protocol.packet.serialize.constants.SerializerAlgorithm;
import com.linlazy.game.protocol.packet.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据包编解码
 */
public class PacketCodeC {

    /**
     * 魔数
     */
    public static final int MAGIC_NUMBER = 0x12345678;


    private static final Map<Byte, Serializer> serializerMap = new HashMap();
    private static final Map<Byte,Class<? extends Packet>> requestPacketMap = new HashMap<>();
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    public PacketCodeC() {

        requestPacketMap.put(RequestCommand.LOGIN_REQUEST, LoginRequestPacket.class);

        serializerMap.put(SerializerAlgorithm.JSON,new JSONSerializer());
    }

    /**
     *  协议编码规则：魔数（4个字节）+ 协议版本号（1个字节）+ 序列化算法（1个字节） + 指令（1个字节） + 数据包长度（4个字节）+数据（N个字节）
     * @param packet
     * @return
     */
    public void encode(ByteBuf byteBuf, Packet packet){
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getProtocolVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public Packet decode(ByteBuf byteBuf){
        //跳过魔数
        byteBuf.skipBytes(4);
        //跳过协议版本号
        byteBuf.skipBytes(1);
        //获取序列化算法
        byte serializeAlgorithm = byteBuf.readByte();
        //获取指令
        byte command = byteBuf.readByte();
        //获取数据包长度
        int bytesLength = byteBuf.readInt();
        //获取数据
        byte[] bytes = new byte[bytesLength];
        byteBuf.readBytes(bytes);

        //通过指令获取对应的指令请求数据包
        Class<? extends Packet> requestPacket =getRequestType(command);
        //通过序列化算法获取序列化方式
        Serializer serializer = getSerializer(serializeAlgorithm);
        if(requestPacket !=null && serializer != null){
            return serializer.deSerialize(bytes, requestPacket);
        }
        System.out.println("包解析错误");
        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }


    private Class<? extends Packet> getRequestType(byte command) {
        return requestPacketMap.get(command);
    }

}
