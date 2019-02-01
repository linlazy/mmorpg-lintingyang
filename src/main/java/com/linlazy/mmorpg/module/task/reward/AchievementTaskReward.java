package com.linlazy.mmorpg.module.task.reward;

import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.server.common.Result;

/**
 * 成就任务奖励
 * @author linlazy
 */
public class AchievementTaskReward extends BaseTaskReward{
    @Override
    protected Integer rewardType() {
        return TaskRewardType.Achievement;
    }

    @Override
    public Result<?> doTaskReward(long actorId, Task task) {
        return Result.success();
    }
}
