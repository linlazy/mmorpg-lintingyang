package com.linlazy.mmorpg.module.task.trigger;

/**
 * 任务触发类型
 * @author linlazy
 */
public interface TaskTriggerType {


    /**
     * 默认触发
     */
    int DEFAULT = 0;

    /**
     * 完成任务触发
     */
    int COMPLETE = 1;

    /**
     * 时间触发
     */
    int TIME = 2;

}
