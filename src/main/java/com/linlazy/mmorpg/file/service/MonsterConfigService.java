package com.linlazy.mmorpg.file.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏场景配置服务类
 * @author linlazy
 */
@Component
public class MonsterConfigService {


    private static ConfigFile monsterConfigFile;

    static {
        monsterConfigFile =  ConfigFileManager.use("config_file/monster_config.json");
    }

    /**
     * 构建场景怪物映射
     */
    private static Map<Integer, List<JSONObject>> sceneMonsterMap = new HashMap<>();


    @PostConstruct
    public void init(){
        JSONArray jsonArray = monsterConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JSONArray sceneIds = jsonObject.getJSONArray("sceneIds");
            for(int j = 0; j < sceneIds.size(); j++){

                Integer sceneId = sceneIds.getInteger(j);
                sceneMonsterMap.computeIfAbsent(sceneId, k -> new ArrayList<>());
                List<JSONObject> jsonObjects = sceneMonsterMap.get(sceneId);
                jsonObjects.add(jsonObject);
            }
        }
    }

    /**
     * 获取场景怪物配置
     * @param sceneId
     * @return
     */
    public List<JSONObject> getMonsterBySceneId(int sceneId){
        List<JSONObject> jsonObjects = sceneMonsterMap.get(sceneId);
        if(jsonObjects == null){
            jsonObjects = Lists.newArrayList();
        }
        return jsonObjects;
    }

}
