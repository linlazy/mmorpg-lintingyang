package com.linlazy.mmorpg.module.common.reward;

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
        return "Reward{" +
                "rewardId=" + rewardId +
                ", count=" + count +
                ", rewardDBType=" + rewardDBType +
                '}';
    }
}
