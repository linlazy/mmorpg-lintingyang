package com.linlazy.mmorpg.file.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.file.config.BossConfig;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import com.linlazy.mmorpg.utils.RewardUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BOSS配置服务类
 * @author linlazy
 */
@Component
public class BossConfigService {


    private static ConfigFile bossConfigFile;

    static {
        bossConfigFile =  ConfigFileManager.use("config_file/boss_config.json");
    }


    /**
     * 构建场景怪物映射
     */
    private static Map<Integer, List<BossConfig>> sceneBossMap = new HashMap<>();

    /**
     * 构建bossId映射
     */
    private static Map<Integer,JSONObject> bossIdMap = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = bossConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JSONArray sceneIds = jsonObject.getJSONArray("sceneIds");
            for(int j = 0; j < sceneIds.size(); j++){

                Integer sceneId = sceneIds.getInteger(j);
                List<BossConfig> bossConfigs = sceneBossMap.computeIfAbsent(sceneId, k -> new ArrayList<>());

                BossConfig bossConfig = new BossConfig();
                int bossId = jsonObject.getIntValue("bossId");
                String name = jsonObject.getString("name");
                int type = jsonObject.getIntValue("type");
                int attack = jsonObject.getIntValue("useSkill");
                int hp = jsonObject.getIntValue("hp");
                String rewards = jsonObject.getString("rewards");
                List<Reward> rewardList = RewardUtils.parseRewards(rewards);
                bossConfig.setRewardList(rewardList);
                bossConfig.setBossId(bossId);
                bossConfig.setHp(hp);
                bossConfig.setName(name);
                bossConfig.setAttack(attack);
                bossConfig.setType(type);

                bossConfigs.add(bossConfig);

            }

            //构建bossId映射
            bossIdMap.put(jsonObject.getIntValue("bossId"),jsonObject);
        }
    }

    /**
     * 获取场景Boss配置
     * @param sceneId
     * @return
     */
    public List<BossConfig> getBossConfigBySceneId(int sceneId){
        List<BossConfig> bossConfigs = sceneBossMap.get(sceneId);
        if(bossConfigs == null){
            bossConfigs = Lists.newArrayList();
        }
        return bossConfigs;
    }

    /**
     * 获取boss配置
     * @param bossId
     * @return
     */
    public JSONObject getBossConfig(int bossId){
        return bossIdMap.get(bossId);
    }
}
