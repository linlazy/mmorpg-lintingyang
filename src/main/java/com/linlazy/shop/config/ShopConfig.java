package com.linlazy.shop.config;

import com.linlazy.common.Reward;
import lombok.Data;

/**
 * 商品配置类
 */
@Data
public class ShopConfig {

    /**
     * 商品ID
     */
    private int goodsId;

    /**
     * 消耗的道具ID
     */
    private int consumeItemId;

    /**
     * 消耗的道具数量
     */
    private int consumeItemCount;

    /**
     * 奖励
     */
    private Reward reward;

    /**
     * 限购次数
     */
    private int limitCount;

    public ShopConfig(int goodsId, int consumeItemId, int consumeItemCount, Reward reward, int limitCount) {
        this.goodsId = goodsId;
        this.consumeItemId = consumeItemId;
        this.consumeItemCount = consumeItemCount;
        this.reward = reward;
        this.limitCount = limitCount;
    }
}
