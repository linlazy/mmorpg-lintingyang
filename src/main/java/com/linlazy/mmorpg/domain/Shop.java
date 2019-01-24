package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.entity.ShopEntity;
import com.linlazy.mmorpg.file.config.ShopConfig;
import com.linlazy.mmorpg.file.service.ShopConfigService;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

import java.util.List;

/**
 * 商城领域类
 * @author linlazy
 */
@Data
public class Shop {

    /**
     * 玩家ID
     */
    private long actorId;
    /**
     * 商品ID
     */
    private long goodsId;

    /**
     * 重置类型
     */
    private int resetType;

    /**
     * 限购次数
     */
    private int limitBuyCount;

    /**
     * 已经购买次数
     */
    private int hasBuyCount;

    /**
     * 下一次重置事件
     */
    private long nextResetTime;

    /**
     * 金币类型
     */
    private int moneyType;
    /**
     * 金币数量
     */
    private int consumeMoneyCount;

    /**
     * 商品奖励
     */
    private List<Reward> rewardList;

    public Shop(long goodsId) {

        ShopConfigService shopConfigService = SpringContextUtil.getApplicationContext().getBean(ShopConfigService.class);
        ShopConfig goodsConfig = shopConfigService.getGoodsConfig(goodsId);
        resetType = goodsConfig.getResetType();
        limitBuyCount = goodsConfig.getLimitTimes();
        moneyType = goodsConfig.getMoneyType();
        consumeMoneyCount = goodsConfig.getMoneyCount();
        rewardList = goodsConfig.getRewardList();
    }


    public Shop(ShopEntity shopEntity) {
         goodsId = shopEntity.getGoodsId();
         actorId = shopEntity.getActorId();
        hasBuyCount = shopEntity.getHasBuyTimes();
         nextResetTime = shopEntity.getNextResetTime();

        ShopConfigService shopConfigService = SpringContextUtil.getApplicationContext().getBean(ShopConfigService.class);
        ShopConfig goodsConfig = shopConfigService.getGoodsConfig(this.goodsId);
         resetType = goodsConfig.getResetType();
        limitBuyCount = goodsConfig.getLimitTimes();
        moneyType = goodsConfig.getMoneyType();
        consumeMoneyCount = goodsConfig.getMoneyCount();
        rewardList = goodsConfig.getRewardList();


    }

    public ShopEntity convertEntity() {
        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setActorId(actorId);
        shopEntity.setGoodsId(goodsId);
        shopEntity.setHasBuyTimes(hasBuyCount);
        shopEntity.setNextResetTime(nextResetTime);
        return shopEntity;
    }
}
