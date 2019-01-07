package com.linlazy.mmorpglintingyang.server.db;

/**
 * @author linlazy
 */
public interface EntityOperatorType {
    /**
     * 增
     */
    int INSERT = 1;
    /**
     * 删
     */
    int DELETE = 2;
    /**
     * 查
     */
    int SELECT = 3;
    /**
     * 改
     */
    int UPDATE = 4;
}
