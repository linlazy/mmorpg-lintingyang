package com.linlazy.common;

import lombok.Data;

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
    private RewardType rewardType;

    public Reward(int rewardId, int count, RewardType rewardType) {
        this.rewardId = rewardId;
        this.count = count;
        this.rewardType = rewardType;
    }
}
