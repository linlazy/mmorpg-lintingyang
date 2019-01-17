package com.linlazy.mmorpg.module.common.reward;

import com.google.common.collect.Lists;
import com.linlazy.mmorpg.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.domain.ItemContext;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linlazy
 */
@Component
public class RewardService {


    @Autowired
    private PlayerBackpackService playerBackpackService;

    /**
     * 发放奖励
     * @param actorId
     * @param rewardList
     */
    public void addRewardList(long actorId, List<Reward> rewardList) {

        for(Reward reward: rewardList){

            ItemContext itemContext = new ItemContext(reward.getRewardId());
            itemContext.setCount(reward.getCount());
            Result<?> push = playerBackpackService.push(actorId, Lists.newArrayList(itemContext));
        }
    }

    /**
     * 消耗奖励
     * @param actorId
     * @param rewardList
     */
    public void consumeRewardList(long actorId, List<Reward> rewardList) {


    }


    /**
     * 是否足够消耗
     * @param actorId
     * @param reward
     */
    public Result<?> isEnoughConsume(long actorId, Reward reward) {

        return Result.success();
    }
}
