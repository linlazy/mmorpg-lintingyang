package com.linlazy.mmorpg.module.common.reward;

import com.google.common.collect.Lists;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.item.domain.ItemContext;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.module.player.service.PlayerService;
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


            if(reward.getRewardId() == RewardID.HP){
                player.resumeHP(reward.getCount());
                playerService.updatePlayer(player);
            }else if(reward.getRewardId() == RewardID.MP){
                player.resumeMP(reward.getCount());
                playerService.updatePlayer(player);
            }else if(reward.getRewardId() == RewardID.GOLD){
                player.resumeGold(reward.getCount());
                playerService.updatePlayer(player);
            }else if(reward.getRewardId() == RewardID.EXP){
                player.addExp(reward.getCount());
                playerService.updatePlayer(player);
            }else {
                ItemContext itemContext = new ItemContext(reward.getRewardId());
                itemContext.setCount(reward.getCount());
                playerBackpackService.push(actorId, Lists.newArrayList(itemContext));
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
