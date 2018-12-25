package com.linlazy.mmorpglintingyang.module.common.reward;

import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
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

    public void addRewardList(long actorId, List<Reward> rewardList) {


        for(Reward reward: rewardList){
            switch (reward.getRewardDBType()){
                case RewardDBType.User:
                    Map<Integer, Integer> attrChangeMap = userManager.addOrConsumeReward(actorId, reward);
                    UserPushHelper.pushActorAttrChange(actorId,attrChangeMap);
                    break;
                case RewardDBType.Item:
                    itemManager.addReward(actorId,reward);
                    break;

            }
        }
    }


    /**
     * 是否足够消耗
     * @param actorId
     * @param reward
     */
    public Result<?> isEnoughtConsuem(long actorId, Reward reward) {
        Map<Integer, Integer> attrChangeMap;

        switch (reward.getRewardId()){
            case RewardID.HP:

                break;
            case RewardID.MP:
                break;
            default:
                itemManager.addReward(actorId,reward);
        }

        return Result.success();
    }
}
