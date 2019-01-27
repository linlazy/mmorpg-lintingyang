package com.linlazy.mmorpg.module.shop.count.reset;

/**
 * @author linlazy
 */
public interface ResetType {
    /**
     * 每日重置
     */
    int DAY = 1;
    /**
     * 每周重置
     */
    int WEEK = 2;
    /**
     * 不重置
     */
    int NONE = 0;
}
