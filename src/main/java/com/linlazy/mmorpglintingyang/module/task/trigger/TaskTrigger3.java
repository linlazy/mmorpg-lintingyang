package com.linlazy.mmorpglintingyang.module.task.trigger;

import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;

public class TaskTrigger3 extends TaskTrigger {
    @Override
    protected int triggerType() {
        return 3;
    }

    @Override
    public boolean isTrigger(TaskDo taskDo) {
        return false;
    }
}
