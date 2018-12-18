package com.linlazy.mmorpglintingyang.module.common.reward;

import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.push.UserPushHelper;
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
}
