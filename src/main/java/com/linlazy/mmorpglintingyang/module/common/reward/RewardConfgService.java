package com.linlazy.mmorpglintingyang.module.common.reward;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.ConfigFile;
import com.linlazy.mmorpglintingyang.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 奖励配置服务
 */
@Component
public class RewardConfgService {

    private static ConfigFile rewardConfigFile;

    static {
        rewardConfigFile =  ConfigFileManager.use("config_file/reward_config.json");
    }

    private static  final Map<Integer, JSONObject> map = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = rewardConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);



            map.put(jsonObject.getInteger("rewardId"),jsonObject);
        }
    }

    public int getRewardDBType(int rewardId) {
        return map.get(rewardId).getIntValue("rewardDBType");
    }
}