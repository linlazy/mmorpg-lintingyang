package com.linlazy.mmorpg.module.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import org.junit.Test;

public class ConfigFileManagerTest {

    @Test
    public void use() {

        ConfigFile configFile = ConfigFileManager.use("config_file/scene_config.json");
        JSONArray jsonObject = configFile.getJsonArray();
        System.out.println(JSON.toJSONString(jsonObject));
        ConfigFile configFile2 = ConfigFileManager.use("config_file/scene2_config.json");
        JSONArray jsonObject2 = configFile2.getJsonArray();
        System.out.println(JSON.toJSONString(jsonObject2));
    }


}