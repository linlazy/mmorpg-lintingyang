package com.linlazy.mmorpg.file.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.file.config.LevelFightConfig;
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
public class LevelFightConfigService {

    private static ConfigFile levelConfigFile;


    static {
        levelConfigFile =  ConfigFileManager.use("config_file/level_fight_config.json");
    }

    private static  Map<Integer, LevelFightConfig>  levelFightConfigMap=new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = levelConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            LevelFightConfig levelFightConfig = new LevelFightConfig();
            int professionId = jsonObject.getIntValue("professionId");
            int attack = jsonObject.getIntValue("attack");
            int defense = jsonObject.getIntValue("defense");
            levelFightConfig.setProfessionId(professionId);
            levelFightConfig.setAttack(attack);
            levelFightConfig.setDefense(defense);

            levelFightConfigMap.put(levelFightConfig.getProfessionId(),levelFightConfig);
        }
    }


    /**
     * 获取职业等级战斗属性配置配置
     * @param professionId
     * @return
     */
    public LevelFightConfig getLevelFightConfig(int professionId){
        return levelFightConfigMap.get(professionId);
    }

}
