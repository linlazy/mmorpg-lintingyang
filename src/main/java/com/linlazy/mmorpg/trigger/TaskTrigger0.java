package com.linlazy.mmorpg.trigger;

import com.linlazy.mmorpg.domain.Task;
import org.springframework.stereotype.Component;

/**
 * 无触发前置条件
 * @author linlazy
 */
@Component
public class TaskTrigger0 extends BaseTaskTrigger {
    @Override
    protected int triggerType() {
        return 0;
    }


    @Override
    public boolean isTrigger(Task taskDo) {
       return true;
    }
}
