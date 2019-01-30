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
     * 已开启，未接受
     */
    int START_UN_ACCEPT = 1;
    /**
     * 已接受，未完成
     */
    int ACCEPT_UN_COMPLETE = 2;
    /**
     * 已完成，未领奖
     */
    int COMPLETE_UN_REWARD = 3;
    /**
     * 已领奖
     */
    int REWARDED = 4;
}
