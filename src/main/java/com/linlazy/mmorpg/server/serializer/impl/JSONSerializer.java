package com.linlazy.mmorpg.server.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.linlazy.mmorpg.server.serializer.Serializer;

/**
 * JSON序列化，使用fastjson支持
 * @author linlazy
 */
public class JSONSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deSerialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
