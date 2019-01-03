package com.linlazy.mmorpglintingyang.module.config;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpglintingyang.server.common.ConfigFile;
import com.linlazy.mmorpglintingyang.server.common.ConfigFileManager;
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


    //构建场景怪物映射
    private static Map<Integer, List<JSONObject>> sceneBossMap = new HashMap<>();

    //构建bossId映射
    private static Map<Integer,JSONObject> bossIdMap = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = bossConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JSONArray sceneIds = jsonObject.getJSONArray("sceneIds");
            for(int j = 0; j < sceneIds.size(); j++){

                Integer sceneId = sceneIds.getInteger(j);
                sceneBossMap.computeIfAbsent(sceneId, k -> new ArrayList<>());
                List<JSONObject> jsonObjects = sceneBossMap.get(sceneId);
                jsonObjects.add(jsonObject);
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
    public List<JSONObject> getBossBySceneId(int sceneId){
        List<JSONObject> jsonObjects = sceneBossMap.get(sceneId);
        if(jsonObjects == null){
            jsonObjects = Lists.newArrayList();
        }
        return jsonObjects;
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
