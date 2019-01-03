package com.linlazy.mmorpglintingyang.module.task.trigger;

import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class BaseTaskTrigger {

    private static Map<Integer, BaseTaskTrigger> map = new HashMap<>();

    @PostConstruct
    protected void init(){
        map.put(triggerType(),this);
    }

    /**
     * 触发方式
     * @return 返回触发方式
     */
    protected abstract int triggerType();

    public static BaseTaskTrigger getTaskTrigger(int triggerType){
        return map.get(triggerType);
    }

    /**
     * 是否触发
     * @param taskDo  任务领域类
     * @return 返回是否触发额结果
     */
    public abstract boolean isTrigger(TaskDo taskDo);

}
