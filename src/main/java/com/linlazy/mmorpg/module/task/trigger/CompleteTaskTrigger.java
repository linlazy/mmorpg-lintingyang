package com.linlazy.mmorpg.module.task.trigger;

import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.module.task.domain.TriggerCondition;
import com.linlazy.mmorpg.module.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 完成任务xxx,触发任务
 * @author linlazy
 */
@Component
public class CompleteTaskTrigger extends BaseTaskTrigger {
    @Override
    protected int triggerType() {
        return TaskTriggerType.COMPLETE;
    }

    @Autowired
    private TaskService taskService;

    @Override
    public boolean isTrigger(Task task) {
        TriggerCondition triggerCondition = task.getTriggerConditionMap().get(triggerType());
        long taskId = triggerCondition.getTriggerArgs().getLongValue("taskId");
        Task targetTask = taskService.getPlayerTask(task.getActorId()).getMap().get(taskId);
        return targetTask.getStatus() >= TaskStatus.COMPLETE_UN_REWARD;
    }
}
