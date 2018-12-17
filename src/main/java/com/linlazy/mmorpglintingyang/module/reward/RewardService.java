package com.linlazy.mmorpglintingyang.module.reward;

import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RewardService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private ItemManager itemManager;

    public void addRewardList(long actorId,List<Reward> rewardList) {
        for(Reward reward: rewardList){
            switch (reward.getRewardDBType()){
                case RewardDBType.User:
                    userManager.addOrConsumeReward(actorId,reward);
                    break;
                case RewardDBType.Item:
                    itemManager.addReward(actorId,reward);
                    break;

            }
        }
    }
}
