package com.linlazy.mmorpglintingyang.module.scene.config;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.ConfigFile;
import com.linlazy.mmorpglintingyang.module.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
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

    private static Map<Integer,JSONObject> map = new HashMap<>();


    @PostConstruct
    public void init(){
        JSONArray jsonArray = npcConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            map.put(jsonObject.getInteger("npcId"),jsonObject);
        }
    }


}
