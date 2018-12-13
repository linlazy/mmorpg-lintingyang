package com.linlazy.mmorpglintingyang.module.item.manager.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.ConfigFile;
import com.linlazy.mmorpglintingyang.module.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 道具配置服务类
 */
@Component
public class ItemConfigService {

    private static ConfigFile itemConfigFile;

    static {
        itemConfigFile =  ConfigFileManager.use("config_file/item_config.json");
    }

    //构建道具ID与道具类型映射
    private static final Map<Integer,Integer> itemIdRewardTypeMap = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = itemConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            itemIdRewardTypeMap.put(jsonObject.getInteger("itemId"),jsonObject.getIntValue("RewardType"));
        }
    }

    /**
     * 获取道具类型
     * @param itemId
     * @return
     */
    public int getRewardTypeByItemId(int itemId){
        return itemIdRewardTypeMap.get(itemId);
    }

    public JSONObject getItemConfig(int itemId) {
        return null;
    }
}
