package com.linlazy.mmorpglintingyang.module.task.entity;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class Task {

    /**
     * 任务ID
     */
    private long taskId;

    /**
     * 玩家ID
     */
    private long actorId;
    /**
     * 任务状态
     */
    private int status;
    /**
     * 任务数据
     */
    private String data;
}
