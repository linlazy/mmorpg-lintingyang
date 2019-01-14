package com.linlazy.mmorpg.file.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 技能配置服务类
 * @author linlazy
 */
@Component
public class SkillConfigService {


    private static ConfigFile skillConfigFile;
    static {
        skillConfigFile =  ConfigFileManager.use("config_file/skill_config.json");
    }

    /**
     * 构建技能ID与配置映射
     */
    private static final Map<Integer,JSONObject> SKILL_ID_MAP = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = skillConfigFile.getJsonArray();
        for(int i =0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int skillId = jsonObject.getIntValue("skillId");
            SKILL_ID_MAP.putIfAbsent(skillId, jsonObject);
        }
    }

    /**
     * 获取技能配置信息
     * @return
     */
    public JSONArray getSkillConfigInfo(){
        return skillConfigFile.getJsonArray();
    }

    public JSONObject getSkillConfig(int skillId) {
        return SKILL_ID_MAP.get(skillId);
    }
}
