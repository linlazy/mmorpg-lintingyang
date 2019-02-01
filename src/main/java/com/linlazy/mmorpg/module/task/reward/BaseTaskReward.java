package com.linlazy.mmorpg.module.task.reward;

import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务奖励
 * @author linlazy
 */
public abstract class BaseTaskReward {

    private static Map<Integer, BaseTaskReward> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(rewardType(),this);
    }

    /**
     * 奖励方式，由子类决定
     * @return
     */
    protected abstract Integer rewardType();

    public static BaseTaskReward getBaseTaskReward(int rewardType){
        return map.get(rewardType);
    }

    /**
     * 执行任务奖励发放
     * @return
     */
    public abstract Result<?> doTaskReward(long actorId,Task task);
}
