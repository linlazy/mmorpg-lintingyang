package com.linlazy.mmorpglintingyang.server.common;

import com.alibaba.fastjson.JSONArray;
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
     * 获取MP回复间隔,每次回复1点MP
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

    /**
     * 获取背包最大格子数
     * @return
     */
    public int getPackageMaxLatticeNum(){
        return jsonObject.getIntValue("packageMaxLatticeNum");
    }

    /**
     * 获取玩家初始场景
     * @return
     */
    public int getInitScene(){
        return jsonObject.getIntValue("initScene");
    }


    /**
     * 是否为副本场景
     * @param sceneId
     * @return
     */
    public boolean isCopy(int sceneId) {
        JSONArray copySceneIds = jsonObject.getJSONArray("copySceneIds");
        for(int i=0 ; i < copySceneIds.size(); i++){
            if(copySceneIds.getIntValue(i) == sceneId){
                return true;
            }
        }
        return false;
    }

    /**
     * 该场景是否为竞技场
     * @param sceneId
     * @return
     */
    public boolean isArena(int sceneId) {
        JSONArray copySceneIds = jsonObject.getJSONArray("arenaSceneIds");
        for(int i=0 ; i < copySceneIds.size(); i++){
            if(copySceneIds.getIntValue(i) == sceneId){
                return true;
            }
        }
        return false;
    }
}
