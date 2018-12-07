package com.linlazy.game.protocol.packet.serialize;

import com.linlazy.game.protocol.packet.serialize.impl.JSONSerializer;

public interface Serializer {

    Serializer DEFAULT = new JSONSerializer();
    /**
     * 获取序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     *  Java对象转二进制
     * @param o java对象
     * @return
     */
    byte[] serialize(Object o);


    /**
     * 二进制转对象
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
     <T> T deSerialize(byte[] bytes,Class<T> clazz);
}
