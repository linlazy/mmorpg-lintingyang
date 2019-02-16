package com.linlazy.mmorpg.module.task.reward;

import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 道具任务奖励
 * @author linlazy
 */
@Component
public class ItemTaskReward extends BaseTaskReward{

    @Autowired
    private RewardService rewardService;

    @Override
    protected Integer rewardType() {
        return TaskRewardType.ITEM;
    }

    @Override
    public Result<?> doTaskReward(long actorId,Task task) {
        List<Reward> rewardList = task.getRewardList();
        rewardService.addRewardList(actorId,rewardList);
        return Result.success();
    }
}
