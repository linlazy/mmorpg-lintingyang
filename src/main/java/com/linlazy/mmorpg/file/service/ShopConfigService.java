package com.linlazy.mmorpg.file.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.file.config.ShopConfig;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import com.linlazy.mmorpg.utils.RewardUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linlazy
 */
@Component
public class ShopConfigService {


    private static ConfigFile shopConfigFile;

    static {
        shopConfigFile =  ConfigFileManager.use("config_file/shop_config.json");
    }

    private static Map<Long, ShopConfig> map = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = shopConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            long goodsId = jsonObject.getLongValue("goodsId");
            String name = jsonObject.getString("name");
            String rewards = jsonObject.getString("rewards");
            int moneyType = jsonObject.getIntValue("moneyType");
            int moneyNum = jsonObject.getIntValue("moneyNum");
            int resetType = jsonObject.getIntValue("resetType");
            int limitTimes = jsonObject.getIntValue("limitTimes");

            ShopConfig shopConfig = new ShopConfig();
            shopConfig.setGoodsId(goodsId);
            shopConfig.setName(name);
            List<Reward> rewardList = RewardUtils.parseRewards(rewards);
            shopConfig.setRewardList(rewardList);
            shopConfig.setMoneyType(moneyType);
            shopConfig.setMoneyCount(moneyNum);
            shopConfig.setResetType(resetType);
            shopConfig.setLimitTimes(limitTimes);

            map.put(shopConfig.getGoodsId(),shopConfig);
        }
    }

    public ShopConfig getGoodsConfig(long goodsId) {
        return map.get(goodsId);
    }
}
