package com.linlazy.mmorpglintingyang.module.common.reward;

import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.module.user.push.UserPushHelper;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RewardService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private ItemManager itemManager;


    /**
     * 发放奖励
     * @param actorId
     * @param rewardList
     */
    public void addRewardList(long actorId, List<Reward> rewardList) {

        for(Reward reward: rewardList){
            if(reward.getRewardId() ==  RewardID.HP
            || reward.getRewardId() ==  RewardID.MP
            || reward.getRewardId() ==  RewardID.GOLD){

                Map<Long, Integer> attrChangeMap = userManager.addOrConsumeReward(actorId, reward);
                UserPushHelper.pushActorAttrChange(actorId,attrChangeMap);

            }else{
                itemManager.addReward(actorId, reward);
            }
        }
    }

    /**
     * 消耗奖励
     * @param actorId
     * @param rewardList
     */
    public void consumeRewardList(long actorId, List<Reward> rewardList) {

        for(Reward reward: rewardList){
            if(reward.getRewardId() ==  RewardID.HP
                    || reward.getRewardId() ==  RewardID.MP
                    || reward.getRewardId() ==  RewardID.GOLD){

                Map<Long, Integer> attrChangeMap = userManager.addOrConsumeReward(actorId, reward);
                UserPushHelper.pushActorAttrChange(actorId,attrChangeMap);

            }else{
                itemManager.addReward(actorId, reward);
            }
        }
    }


    /**
     * 是否足够消耗
     * @param actorId
     * @param reward
     */
    public Result<?> isEnoughConsume(long actorId, Reward reward) {
        User user = userManager.getUser(actorId);

        if (reward.getRewardId() == RewardID.HP) {
            if (user.getHp() < reward.getCount()) {
                return Result.valueOf("HP不足");
            }
            return Result.success();
        } else if(reward.getRewardId() == RewardID.MP){
            if(user.getMp() < reward.getCount()){
                return Result.valueOf("MP不足");
            }
            return Result.success();
        } else if(reward.getRewardId() == RewardID.GOLD){
            if(user.getMp() < reward.getCount()){
                return Result.valueOf("金币不足");
            }
            return Result.success();
        }else {
            Item item = itemManager.getItem(actorId, reward.getRewardId());
            if(item == null || item.getCount() < reward.getCount()){
                return Result.valueOf("道具不足");
            }
        }
        return Result.success();
    }
}
