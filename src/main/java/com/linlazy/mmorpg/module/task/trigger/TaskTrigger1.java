package com.linlazy.mmorpg.module.task.trigger;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.dao.TaskDAO;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 完成任务xxx,触发任务
 * @author linlazy
 */
public class TaskTrigger1 extends BaseTaskTrigger {
    @Override
    protected int triggerType() {
        return 1;
    }

    @Autowired
    private TaskDAO taskDao;

    @Override
    public boolean isTrigger(Task taskDo) {
        JSONObject triggerArgs = taskDo.getTriggerArgs();
        int taskId = triggerArgs.getIntValue("taskId");
        TaskEntity task = taskDao.getEntityByPK(taskDo.getActorId(), taskId);
        return task.getStatus() >= TaskStatus.COMPLETE_UN_REWARD;
    }
}
