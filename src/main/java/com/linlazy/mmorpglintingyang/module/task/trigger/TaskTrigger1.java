package com.linlazy.mmorpglintingyang.module.task.trigger;

import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;

public class TaskTrigger1 extends TaskTrigger {
    @Override
    protected int triggerType() {
        return 1;
    }

    @Override
    public boolean isTrigger(TaskDo taskDo) {
        return false;
    }
}
