package com.linlazy.mmorpglintingyang.module.shop.validator;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardID;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardService;
import com.linlazy.mmorpglintingyang.module.shop.config.ShopConfigService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShopValidator {

    @Autowired
    private ShopConfigService shopConfigService;

    @Autowired
    private RewardService rewardService;

    public boolean isNotExist(long goodsId){
        return shopConfigService.getGoodsConfig(goodsId) == null;
    }

    public boolean isNotEnoughConsume(long actorId,long goodsId,int num){
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
