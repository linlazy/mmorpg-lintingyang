package com.linlazy.mmorpglintingyang.module.item.manager.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.ConfigFile;
import com.linlazy.mmorpglintingyang.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    private static  final Map<Integer,JSONObject> map = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = itemConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);


            map.put(jsonObject.getInteger("itemId"),jsonObject);
        }
    }

    public JSONObject getItemConfig(int itemId) {
        return map.get(itemId);
    }
}
