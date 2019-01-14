package com.linlazy.mmorpg.trigger;

import com.linlazy.mmorpg.dao.TaskDAO;
import com.linlazy.mmorpg.domain.Task;
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
    private TaskDAO taskDao;

    @Override
    public boolean isTrigger(Task task) {
        return false;
    }
}
