package com.linlazy.mmorpg.module.task.condition.start;

import com.linlazy.mmorpg.module.task.domain.Task;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 开启条件
 * @author linlazy
 */
public abstract class StartCondition {

    private static Map<Integer,StartCondition> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(startConditionType(),this);
    }

    /**
     * 开启任务条件类型，由子类决定
     * @return
     */
    protected abstract Integer startConditionType();

    public static StartCondition getStartCondition(int startConditionType){
        return map.get(startConditionType);
    }

    /**
     * 是否达成开启任务条件
     * @param actorId 玩家ID
     * @param task 任务
     * @return
     */
    public abstract boolean isReachCondition(long actorId, Task task);

    /**
     * 前端输出
     * @param actorId
     * @param task
     * @return
     */
    public abstract String startConditionString(long actorId, Task task);
}
