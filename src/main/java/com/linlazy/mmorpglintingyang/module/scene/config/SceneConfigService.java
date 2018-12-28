package com.linlazy.mmorpglintingyang.module.scene.config;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.ConfigFile;
import com.linlazy.mmorpglintingyang.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 游戏场景配置服务类
 */
@Component
public class SceneConfigService {


    private static ConfigFile sceneConfigFile;

    static {
        sceneConfigFile =  ConfigFileManager.use("config_file/scene_config.json");
    }

    private static Map<Integer,JSONObject> map = new HashMap<>();

    private int initSceneId;

    @PostConstruct
    public void init(){
        JSONArray jsonArray = sceneConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            map.put(jsonObject.getInteger("sceneId"),jsonObject);
            if(jsonObject.getBoolean("init")!= null){
                initSceneId = jsonObject.getIntValue("sceneId");
            }
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
        JSONObject jsonObject = map.get(currentSceneId);
        JSONArray neighborSet = jsonObject.getJSONArray("neighborSet");
        for(int i = 0; i < neighborSet.size(); i ++){
            if(neighborSet.getIntValue(i) == targetSceneId){
                return true;
            }
        }
        return false;
    }


    /**
     * 获取副本配置
     * @param sceneId
     * @return
     */
    public JSONObject getCopyConfig(int sceneId) {
        return map.get(sceneId);
    }
}
