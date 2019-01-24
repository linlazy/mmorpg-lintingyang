package com.linlazy.mmorpg.module.common.reward;

import com.linlazy.mmorpg.domain.Item;
import com.linlazy.mmorpg.dto.ItemDTO;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class Reward {

    /**
     * 奖励ID
     */

    private int rewardId;
    /**
     * 奖励数量
     */
    private int count;

    /**
     * 奖励类型
     */
    private int rewardDBType;

    public Reward() {
    }

    public Reward(int rewardId, int count, int rewardDBType) {
        this.rewardId = rewardId;
        this.count = count;
        this.rewardDBType = rewardDBType;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();


        switch (rewardId){
            case RewardID.HP:
                stringBuilder.append("HP");
                stringBuilder.append(String.format("数量【%d】",count));
                break;
            case RewardID.MP:
                stringBuilder.append("MP");
                stringBuilder.append(String.format("数量【%d】",count));
                break;
            case RewardID.GOLD:
                stringBuilder.append("金币");
                stringBuilder.append(String.format("数量【%d】",count));
                break;
            default:
                stringBuilder.append(new ItemDTO(new Item(rewardId,count)).toString());
        }




        return stringBuilder.toString();
    }
}
