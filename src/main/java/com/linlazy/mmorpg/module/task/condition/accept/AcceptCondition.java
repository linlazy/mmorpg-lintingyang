package com.linlazy.mmorpg.module.task.condition.accept;

import com.linlazy.mmorpg.module.task.domain.Task;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 接受任务条件
 * @author linlazy
 */
public abstract class AcceptCondition {

    private static Map<Integer,AcceptCondition> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(acceptConditionType(),this);
    }

    /**
     * 接受任务条件类型，由子类决定
     * @return
     */
    protected abstract Integer acceptConditionType();


    public static AcceptCondition getAcceptCondition(int acceptConditionType){
        return map.get(acceptConditionType);
    }

    /**
     * 是否达成接受任务条件
     * @param actorId 玩家ID
     * @param task 任务
     * @return
     */
    public abstract boolean isReachCondition(long actorId, Task task);

    /**
     * 前端打印
     * @param actorId
     * @param task
     * @return
     */
    public abstract String acceptConditionString(long actorId, Task task);
}
