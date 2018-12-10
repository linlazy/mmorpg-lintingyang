package com.linlazy.shop.config;

import com.linlazy.common.Reward;
import com.linlazy.common.RewardType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品配置服务类
 */
@Component
public class ShopConfigService {

    private Map<Integer,ShopConfig> shopConfigMap = new HashMap();

    @PostConstruct
    public void init(){
        shopConfigMap.put(1,new ShopConfig(1,6001,50, new Reward(1,2, RewardType.EQUIP),5));
        shopConfigMap.put(2,new ShopConfig(2,6002,50, new Reward(2,2, RewardType.CONSUMABLES),5));
        shopConfigMap.put(3,new ShopConfig(3,6003,50, new Reward(3,2, RewardType.CONSUMABLES),5));
    }

    public boolean hasGoods(int goodsId) {
        return shopConfigMap.get(goodsId) != null;
    }
}
