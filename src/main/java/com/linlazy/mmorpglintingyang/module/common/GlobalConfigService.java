package com.linlazy.mmorpglintingyang.module.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GlobalConfigService {
    private static ConfigFile globalConfigFile;

    static {
        globalConfigFile =  ConfigFileManager.use("config_file/global_config.json");
    }

    private  JSONObject jsonObject;

    @PostConstruct
    public void init(){
        jsonObject = globalConfigFile.getJsonArray().getJSONObject(0);
    }

    /**
     * 获取MP回复间隔
     * @return
     */
    public long getMPResumeIntervalMills(){
        return jsonObject.getIntValue("MP_resumeInterval")*1000;
    }

    /**
     * 获取HP回复间隔
     * @return
     */
    public long getHPResumeIntervalMills(){
        return jsonObject.getIntValue("HP_resumeInterval")*1000;
    }
}
