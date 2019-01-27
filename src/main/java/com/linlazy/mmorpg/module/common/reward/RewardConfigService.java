package com.linlazy.mmorpg.module.common.reward;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 奖励配置服务
 * @author linlazy
 */
@Component
public class RewardConfigService {

    private static ConfigFile rewardConfigFile;

    static {
        rewardConfigFile =  ConfigFileManager.use("config_file/reward_config.json");
    }

    private static  final Map<Long, JSONObject> MAP = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = rewardConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            MAP.put(jsonObject.getLongValue("rewardId"),jsonObject);
        }
    }

    public int getRewardDBType(long rewardId) {
        return MAP.get(rewardId).getIntValue("rewardDBType");
    }

    public List<Reward> parseRewards(String rewards){
        List<Reward> rewardList = new ArrayList<>();

        JSONArray jsonArray = JSON.parseArray(rewards);
        for(int i = 0 ; i < jsonArray.size(); i ++){
            JSONArray jsonArray1 = jsonArray.getJSONArray(i);

            Reward reward = new Reward();
            long rewardId = jsonArray1.getLongValue(0);
            int rewardCount = jsonArray1.getIntValue(1);

            reward.setRewardId(rewardId);
            reward.setCount(rewardCount);
            reward.setRewardDBType(getRewardDBType(rewardId));
            rewardList.add(reward);
        }

        return rewardList;
    }
}
