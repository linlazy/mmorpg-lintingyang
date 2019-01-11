package com.linlazy.mmorpg.module.task.constants;

/**
 * @author linlazy
 */
public interface TaskStatus {
    /**
     * 未开启
     */
    int UN_START = 0;
    /**
     * 已开启，未完成
     */
    int START_UNCOMPLETE = 1;
    /**
     * 已完成，未领奖
     */
    int COMPLETE_UNREWARD = 2;
    /**
     * 已领奖
     */
    int REWARDED = 3;
}
