package com.linlazy.mmorpg.file.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.file.config.LevelConfig;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
@Component
public class LevelConfigService {

    private static ConfigFile levelConfigFile;

    static {
        levelConfigFile =  ConfigFileManager.use("config_file/level_config.json");
    }

    /**
     * 构建等级配置映射
     */
    private static Map<Integer, LevelConfig> levelConfigMap = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = levelConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int level = jsonObject.getIntValue("level");
            int maxExp = jsonObject.getIntValue("maxExp");
            int addMaxHp = jsonObject.getIntValue("addMaxHp");

            LevelConfig levelConfig = new LevelConfig();
            levelConfig.setLevel(level);
            levelConfig.setMaxExp(maxExp);
            levelConfig.setAddHp(addMaxHp);

            levelConfigMap.put(levelConfig.getLevel(),levelConfig);
        }
    }

    public LevelConfig getLevelConfig(int level){
        return levelConfigMap.get(level);
    }
}
