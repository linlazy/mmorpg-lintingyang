package com.linlazy.mmorpglintingyang.server.db;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class FieldInfo {

    /**
     * 是否为标识
     */
    boolean isIdentity;
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段值
     */
    private String value;
    /**
     * 字段类型
     */
    private String type;
}
