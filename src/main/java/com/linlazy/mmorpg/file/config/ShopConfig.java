package com.linlazy.mmorpg.file.config;

import com.linlazy.mmorpg.module.common.reward.Reward;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城配置类
 * @author linlazy
 */
@Data
public class ShopConfig {
    /**
     * 商品ID
     */
    private long goodsId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 奖励
     */
    private List<Reward> rewardList = new ArrayList<>();

    /**
     * 货币类型
     */
    private int moneyType;
    /**
     * 货币数量
     */
    private int moneyCount;

    /**
     * 重置类型
     */
    private int resetType;

    /**
     * 限购次数
     */
    private int limitTimes;
}
