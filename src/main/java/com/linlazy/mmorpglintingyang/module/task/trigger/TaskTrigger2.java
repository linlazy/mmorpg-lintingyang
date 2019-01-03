package com.linlazy.mmorpglintingyang.module.task.trigger;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.task.constants.TaskStatus;
import com.linlazy.mmorpglintingyang.module.task.dao.TaskDao;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;
import com.linlazy.mmorpglintingyang.module.task.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 任务xxx已领奖，触发任务
 * @author linlazy
 */
public class TaskTrigger2 extends TaskTrigger {
    @Override
    protected int triggerType() {
        return 2;
    }

    @Autowired
    private TaskDao taskDao;

    @Override
    public boolean isTrigger(TaskDo taskDo) {
        JSONObject triggerArgs = taskDo.getTriggerArgs();
        int taskId = triggerArgs.getIntValue("taskId");
        Task task = taskDao.getTask(taskDo.getActorId(), taskId);
        return task.getStatus() >= TaskStatus.START_UNCOMPLETE;
    }
}
