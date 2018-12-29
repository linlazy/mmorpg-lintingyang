package com.linlazy.mmorpglintingyang.module.task.trigger;

import com.linlazy.mmorpglintingyang.module.task.dao.TaskDao;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 开启任务xxx,触发任务
 */
public class TaskTrigger3 extends TaskTrigger {
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
