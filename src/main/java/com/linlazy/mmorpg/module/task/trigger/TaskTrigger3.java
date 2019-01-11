package com.linlazy.mmorpg.module.task.trigger;

import com.linlazy.mmorpg.module.task.dao.TaskDao;
import com.linlazy.mmorpg.module.task.domain.TaskDo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 开启任务xxx,触发任务
 * @author linlazy
 */
public class TaskTrigger3 extends BaseTaskTrigger {
    @Override
    protected int triggerType() {
        return 3;
    }

    @Autowired
    private TaskDao taskDao;

    @Override
    public boolean isTrigger(TaskDo taskDo) {
        return false;
    }
}
