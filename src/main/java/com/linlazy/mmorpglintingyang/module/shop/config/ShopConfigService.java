package com.linlazy.mmorpglintingyang.module.shop.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.ConfigFile;
import com.linlazy.mmorpglintingyang.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
@Component
public class ShopConfigService {


    private static ConfigFile shopConfigFile;

    static {
        shopConfigFile =  ConfigFileManager.use("config_file/shop_config.json");
    }

    private static Map<Long,JSONObject> map = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = shopConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            map.put(jsonObject.getLongValue("goodsId"),jsonObject);
        }
    }

    public JSONObject getGoodsConfig(long goodsId) {
        return map.get(goodsId);
    }
}
