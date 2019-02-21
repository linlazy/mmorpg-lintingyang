package com.linlazy.mmorpg.server.protocol;

import lombok.Data;

/**
 * 自定义协议
 * @author linlazy
 */
@Data
public class Packet {
    /**
     * 协议版本
     */
    private Byte version = 1;

}
