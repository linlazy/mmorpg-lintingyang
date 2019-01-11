package com.linlazy.mmorpg.entity;

import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
@Table("task")
public class TaskEntity extends Entity {

    /**
     * 任务ID
     */
    @Cloumn(pk = true)
    private long taskId;

    /**
     * 玩家ID
     */
    @Cloumn(pk = true)
    private long actorId;
    /**
     * 任务状态
     */
    @Cloumn
    private int status;
    /**
     * 任务数据
     */
    @Cloumn
    private String data;
}
