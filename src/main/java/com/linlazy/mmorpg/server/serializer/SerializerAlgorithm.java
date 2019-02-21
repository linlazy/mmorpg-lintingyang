package com.linlazy.mmorpg.server.serializer;

/**
 * @author linlazy
 */
public interface SerializerAlgorithm {
    /**
     * JSON序列化
     */
    int JSON = 1;
    /**
     * protocol buffer 序列化
     */
    int PB = 2;

    /**
     * xml序列化
     */
    int XML = 3;

    /**
     * thrift序列化
     */
    int THRIFT = 4;
}
