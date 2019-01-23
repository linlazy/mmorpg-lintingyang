package com.linlazy.mmorpg.module.common.reward;

import com.google.common.collect.Lists;
import com.linlazy.mmorpg.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.domain.ItemContext;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.service.PlayerService;
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
    @Autowired
    private PlayerService playerService;

    /**
     * 发放奖励
     * @param actorId
     * @param rewardList
     */
    public void addRewardList(long actorId, List<Reward> rewardList) {
        Player player = playerService.getPlayer(actorId);
        for(Reward reward: rewardList){

        switch (reward.getRewardId()){
            case RewardID.HP:
                player.resumeHP(reward.getCount());
                playerService.updatePlayer(player);
                break;
            case RewardID.MP:
                player.resumeMP(reward.getCount());
                playerService.updatePlayer(player);
                break;
            case RewardID.GOLD:
                player.resumeGold(reward.getCount());
                playerService.updatePlayer(player);
                break;
            default:
            ItemContext itemContext = new ItemContext(reward.getRewardId());
            itemContext.setCount(reward.getCount());
            Result<?> push = playerBackpackService.push(actorId, Lists.newArrayList(itemContext));
        }

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
