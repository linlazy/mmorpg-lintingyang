package com.linlazy.mmorpglintingyang.module.common;

import lombok.Data;

@Data
public class Reward {


    /**
     * 奖励数量
     */
    private int count;

    /**
     * 奖励类型
     */
    private int rewardType;

    public Reward(int count, int rewardType) {
        this.count = count;
        this.rewardType = rewardType;
    }
}
