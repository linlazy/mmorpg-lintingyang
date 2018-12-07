package com.linlazy.game.protocol.packet.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.linlazy.game.protocol.packet.serialize.Serializer;
import com.linlazy.game.protocol.packet.serialize.constants.SerializerAlgorithm;

public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {

        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }

}
