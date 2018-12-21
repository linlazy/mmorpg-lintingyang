package com.linlazy.mmorpglintingyang.module.scene.config;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.ConfigFile;
import com.linlazy.mmorpglintingyang.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏场景配置服务类
 */
@Component
public class NpcConfigService {


    private static ConfigFile npcConfigFile;

    static {
        npcConfigFile =  ConfigFileManager.use("config_file/npc_config.json");
    }

    //构建场景怪物映射
    private static Map<Integer, List<JSONObject>> sceneNpcMap = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = npcConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JSONArray sceneIds = jsonObject.getJSONArray("sceneIds");
            for(int j = 0; j < sceneIds.size(); j++){

                Integer sceneId = sceneIds.getInteger(j);
                sceneNpcMap.computeIfAbsent(sceneId, k -> new ArrayList<>());
                List<JSONObject> jsonObjects = sceneNpcMap.get(sceneId);
                jsonObjects.add(jsonObject);
            }
        }
    }

    /**
     * 获取场景怪物配置
     * @param sceneId
     * @return
     */
    public List<JSONObject> getNpcBySceneId(int sceneId){
        return sceneNpcMap.get(sceneId);
    }

    public boolean hasNPC(int sceneId, int npcId) {
        List<JSONObject> sceneIdNpc= getNpcBySceneId(sceneId);
        return sceneIdNpc.stream()
                .anyMatch(jsonObject -> jsonObject.getIntValue("npcId") == npcId);
    }
}