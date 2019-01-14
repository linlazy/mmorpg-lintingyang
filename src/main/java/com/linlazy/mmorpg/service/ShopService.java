package com.linlazy.mmorpg.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.file.service.ShopConfigService;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.common.reward.RewardID;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class ShopService {


    @Autowired
    private ShopConfigService shopConfigService;
    @Autowired
    private RewardService rewardService;


    public Result<?> buy(long actorId, long goodsId, int num) {

        if(isNotExist(goodsId)){
            return Result.valueOf("商品不存在");
        }

        if(isNotEnoughConsume(actorId,goodsId,num)){
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

    private boolean isNotExist(long goodsId){
        return shopConfigService.getGoodsConfig(goodsId) == null;
    }

    private boolean isNotEnoughConsume(long actorId,long goodsId,int num){
        JSONObject goodsConfig = shopConfigService.getGoodsConfig(goodsId);
        int consumeNum = goodsConfig.getIntValue("consumeNum");
        Reward reward = new Reward();
        reward.setRewardId(RewardID.GOLD);
        reward.setCount(consumeNum * num);
        Result<?> enoughConsume = rewardService.isEnoughConsume(actorId, reward);
        if(enoughConsume.isFail()){
            return true;
        }
        return false;
    }
}
