package com.linlazy.mmorpg.file.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 道具配置服务类
 * @author linlazy
 */
@Component
public class ItemConfigService {

    private static ConfigFile itemConfigFile;

    static {
        itemConfigFile =  ConfigFileManager.use("config_file/item_config.json");
    }

    private static  final Map<Integer,JSONObject> MAP = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = itemConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);


            MAP.put(jsonObject.getInteger("itemId"),jsonObject);
        }
    }

    public JSONObject getItemConfig(int itemId) {
        return MAP.get(itemId);
    }

    public Collection<JSONObject> getAllItemConfig(){
        return MAP.values();
    }
}
