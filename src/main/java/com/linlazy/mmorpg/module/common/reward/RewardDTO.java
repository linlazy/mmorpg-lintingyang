package com.linlazy.mmorpg.module.common.reward;

import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.item.dto.ItemDTO;

/**
 * @author linlazy
 */
public class RewardDTO {

    /**
     * 奖励ID
     */
    private Long rewardId;

    /**
     * 奖励数量
     */
    private Integer count;


    public RewardDTO(Reward reward) {
        this.rewardId = reward.getRewardId();
        this.count = reward.getCount();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(rewardId == RewardID.GOLD){
            stringBuilder.append("【金币】");
            stringBuilder.append(String.format("数量【%d】",count)).append("\r\n");
        }else if(rewardId == RewardID.HP){
            stringBuilder.append("【HP】");
            stringBuilder.append(String.format("数量【%d】",count)).append("\r\n");
        }else if(rewardId == RewardID.MP){
            stringBuilder.append("【MP】");
            stringBuilder.append(String.format("数量【%d】",count)).append("\r\n");
        }else if(rewardId == RewardID.EXP){
            stringBuilder.append("【经验】");
            stringBuilder.append(String.format("数量【%d】",count)).append("\r\n");
        }else {
            stringBuilder.append(new ItemDTO(new Item(rewardId,count)).toString()).append("\r\n");
        }
        return stringBuilder.toString();
    }
}
