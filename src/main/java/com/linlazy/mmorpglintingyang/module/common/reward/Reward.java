package com.linlazy.mmorpglintingyang.module.common.reward;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class Reward {

    /**
     * 奖励ID
     */

    private long rewardId;
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

    public Reward(long rewardId, int count, int rewardDBType) {
        this.rewardId = rewardId;
        this.count = count;
        this.rewardDBType = rewardDBType;
    }
}
