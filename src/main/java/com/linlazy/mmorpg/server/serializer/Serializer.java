package com.linlazy.mmorpg.server.serializer;

/**
 * 序列化
 * @author linlazy
 */
public interface Serializer {

    /**
     * java对象转二进制
     * @param object
     * @return
     */
    byte[] serialize(Object object);


    /**
     * 二进制转Java对象
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deSerialize(Class<T> clazz,byte[] bytes);
}
