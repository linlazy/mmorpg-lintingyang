package com.linlazy.mmorpglintingyang.module.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardID;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardService;
import com.linlazy.mmorpglintingyang.module.shop.config.ShopConfigService;
import com.linlazy.mmorpglintingyang.module.shop.validator.ShopValidator;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShopService {

    @Autowired
    private ShopValidator shopValidator;
    @Autowired
    private ShopConfigService shopConfigService;
    @Autowired
    private RewardService rewardService;


    public Result<?> buy(long actorId, long goodsId, int num) {

        if(shopValidator.isNotExist(goodsId)){
            return Result.valueOf("商品不存在");
        }

        if(shopValidator.isNotEnoughConsume(actorId,goodsId,num)){
            return Result.valueOf("金币不足");
        }


        //扣除消耗
        JSONObject goodsConfig = shopConfigService.getGoodsConfig(goodsId);
        int money = goodsConfig.getIntValue("money");
        int consumeGold = money * num;
        Reward consumeReward = new Reward();
        consumeReward.setRewardId(RewardID.GOLD);
        consumeReward.setCount(consumeGold);
        rewardService.consumeRewardList(actorId,Lists.newArrayList(consumeReward));


        //发奖励
        int itemId = goodsConfig.getIntValue("itemId");
        int itemNum = goodsConfig.getIntValue("itemNum");
        Reward reward = new Reward();
        reward.setRewardId(itemId);
        reward.setCount(itemNum *num);
        rewardService.addRewardList(actorId, Lists.newArrayList(reward));

        return Result.success();
    }
}
