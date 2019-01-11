package com.linlazy.mmorpg.module.task.trigger;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.module.task.dao.TaskDao;
import com.linlazy.mmorpg.module.task.domain.TaskDo;
import com.linlazy.mmorpg.module.task.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 任务xxx已领奖，触发任务
 * @author linlazy
 */
public class TaskTrigger2 extends BaseTaskTrigger {
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
