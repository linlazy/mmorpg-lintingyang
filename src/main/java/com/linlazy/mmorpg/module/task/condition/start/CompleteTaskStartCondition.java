package com.linlazy.mmorpg.module.task.condition.start;

import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.module.task.domain.TriggerCondition;
import com.linlazy.mmorpg.module.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 开启任务条件：完成某任务
 * @author linlazy
 */
@Component
public class CompleteTaskStartCondition extends StartCondition{
    @Override
    protected Integer startConditionType() {
        return StartConditionType.COMPLETE_TASK;
    }


    @Autowired
    private TaskService taskService;

    @Override
    public boolean isReachCondition(long actorId, Task task) {
        TriggerCondition triggerCondition = task.getStartConditionMap().get(startConditionType());
        long taskId = triggerCondition.getTriggerArgs().getLongValue("taskId");
        Task targetTask = taskService.getPlayerTask(task.getActorId()).getMap().get(taskId);
        return targetTask.getStatus() == TaskStatus.COMPLETED;
    }

    @Override
    public String startConditionString(long actorId, Task task) {
        TriggerCondition triggerCondition = task.getStartConditionMap().get(startConditionType());
        long taskId = triggerCondition.getTriggerArgs().getLongValue("taskId");
        return String.format("完成任务【】",taskId);
    }
}
