package com.linlazy.mmorpg.module.task.constants;

/**
 * @author linlazy
 */
public interface TaskStatus {
    /**
     * 未开启任务
     */
    int UN_START = 0;
    /**
     * 已开启任务，未接受任务
     */
    int START_UN_ACCEPT = 1;
    /**
     * 已接受任务，未完成任务
     */
    int ACCEPT_UN_COMPLETE = 2;
    /**
     * 已接受可完成
     */
    int ACCEPT_ABLE_COMPLETE = 3;
    /**
     * 已完成
     */
    int COMPLETED = 4;
}
