package com.linlazy.mmorpglintingyang.module.task.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.ConfigFile;
import com.linlazy.mmorpglintingyang.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class TaskConfigService {

    private static ConfigFile taskConfigFile;


    static {
        taskConfigFile =  ConfigFileManager.use("config_file/task_config.json");
    }

    Set<JSONObject> set = new HashSet<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = taskConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            set.add(jsonObject);
        }
    }

    public Set<JSONObject> getAllTaskConfig() {
        return set;
    }
}
