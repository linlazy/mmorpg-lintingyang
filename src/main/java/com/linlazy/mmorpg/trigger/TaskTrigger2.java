package com.linlazy.mmorpg.trigger;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.constants.TaskStatus;
import com.linlazy.mmorpg.dao.TaskDAO;
import com.linlazy.mmorpg.domain.Task;
import com.linlazy.mmorpg.entity.TaskEntity;
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
    private TaskDAO taskDao;

    @Override
    public boolean isTrigger(Task taskDo) {
        JSONObject triggerArgs = taskDo.getTriggerArgs();
        int taskId = triggerArgs.getIntValue("taskId");
        TaskEntity task = taskDao.getEntityByPK(taskDo.getActorId(), taskId);
        return task.getStatus() >= TaskStatus.START_UNCOMPLETE;
    }
}
