package com.linlazy.mmorpg.file.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.file.config.SceneConfig;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏场景配置服务类
 * @author linlazy
 */
@Component
public class SceneConfigService {

    @Autowired
    private GlobalConfigService globalConfigService;

    private static ConfigFile sceneConfigFile;

    static {
        sceneConfigFile =  ConfigFileManager.use("config_file/scene_config.json");
    }

    private static Map<Integer,SceneConfig> map = new HashMap<>();

    private int initSceneId;

    @PostConstruct
    public void init(){
        JSONArray jsonArray = sceneConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Integer sceneId = jsonObject.getInteger("sceneId");
            String name = jsonObject.getString("name");
            List<Integer> neighborSet = jsonObject.getJSONArray("neighborSet").toJavaList(Integer.class);

            SceneConfig sceneConfig = new SceneConfig();
            sceneConfig.setSceneId(sceneId);
            sceneConfig.setName(name);
            sceneConfig.setNeighborSet(neighborSet);
            sceneConfig.setOverTimeSeconds(jsonObject.getIntValue("times"));

            //初始化奖励
            List<Reward> rewardList = sceneConfig.getRewardList();

            JSONArray rewards = jsonObject.getJSONArray("rewards");
            if(rewards != null){
                for(int j= 0; j< rewards.size();j++){
                    JSONArray reward = rewards.getJSONArray(j);

                    Reward reward1 = new Reward();

                    long itemId = reward.getLongValue(0);
                    int num = reward.getIntValue(1);
                    reward1.setRewardId(itemId);
                    reward1.setCount(num);

                    rewardList.add(reward1);
                }
            }

            map.put(sceneId,sceneConfig);
        }
    }

    /**
     *  场景是否存在
     * @param targetSceneId 玩家前往场景ID
     * @return
     */
    public boolean hasScene(int targetSceneId) {
        JSONArray jsonArray = sceneConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if( jsonObject.getIntValue("sceneId") == targetSceneId){
                return true;
            }

        }
        return false;
    }

    /**
     * 能否从当前场景移动到目标场景
     * @param currentSceneId 玩家所处场景ID
     * @param targetSceneId 玩家移动目标场景ID
     * @return
     */
    public boolean canMoveToTarget(int currentSceneId, int targetSceneId) {
        SceneConfig sceneConfig = map.get(currentSceneId);
        return sceneConfig.getNeighborSet().stream()
                .anyMatch(sceneId ->sceneId == targetSceneId);
    }



    public boolean isCopyScene(int sceneId){
        return globalConfigService.isCopy(sceneId);
    }

    /**
     * 获取场景配置
     * @param sceneId
     * @return
     */
    public SceneConfig getSceneConfig(int sceneId) {
        return map.get(sceneId);
    }

    public boolean isFightCopyScene(int sceneId) {
        return globalConfigService.getFightCopySceneId() == sceneId;
    }
}
