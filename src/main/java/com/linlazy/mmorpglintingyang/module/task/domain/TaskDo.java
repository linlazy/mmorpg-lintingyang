package com.linlazy.mmorpglintingyang.module.task.domain;

import lombok.Data;

@Data
public class TaskDo {

    /**
     * 任务唯一标识
     */
    private long taskId;
    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 任务类型
     */
    private int type;

    /**
     * 任务数据
     */
    private String data;

    /**
     * 任务状态
     */
    private int status;

}
