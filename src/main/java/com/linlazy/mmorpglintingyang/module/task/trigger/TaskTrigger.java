package com.linlazy.mmorpglintingyang.module.task.trigger;

import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class TaskTrigger {

    private static Map<Integer, TaskTrigger> map = new HashMap<>();

    @PostConstruct
    protected void init(){
        map.put(triggerType(),this);
    }

    protected abstract int triggerType();

    public static TaskTrigger getTaskTrigger(int triggerType){
        return map.get(triggerType);
    }

    public abstract boolean isTrigger(TaskDo taskDo);

}
