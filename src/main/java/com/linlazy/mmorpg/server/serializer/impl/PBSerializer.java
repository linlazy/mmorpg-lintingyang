package com.linlazy.mmorpg.server.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.linlazy.PersonModel;
import com.linlazy.mmorpg.server.serializer.Serializer;

/**
 * protocol序列化，
 * @author linlazy
 */
public class PBSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) {
        PersonModel.Person person = PersonModel.Person.newBuilder().build();
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deSerialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
