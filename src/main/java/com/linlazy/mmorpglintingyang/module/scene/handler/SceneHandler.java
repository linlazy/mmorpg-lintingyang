package com.linlazy.mmorpglintingyang.module.scene.handler;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.module.scene.service.SceneService;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
@Module("scene")
public class SceneHandler {

    @Autowired
    private SceneService sceneService;

    /**
     * 移动场景
     * @param jsonObject
     * @return
     */
    @Cmd("move")
    public Result<?> move(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        int targetSceneId = jsonObject.getIntValue("targetSceneId");
        return sceneService.moveTo(actorId,targetSceneId);
    }

    /**
     * 进入场景
     * @param jsonObject
     * @return
     */
    @Cmd("enter")
    public Result<?> enter(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return sceneService.enter(actorId);
    }

//    /**
//     *  获取当前场景实体信息
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("aoi")
//    public Result<?> aoi(JSONObject jsonObject){
//        long actorId = jsonObject.getLong("actorId");
//        return sceneService.aoi(actorId,jsonObject);
//    }

    /**
     *  对话npc
     * @param jsonObject
     * @return
     */
    @Cmd("talk")
    public Result<?> talk(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        int npcId = jsonObject.getIntValue("npcId");
        return sceneService.talk(actorId,npcId,jsonObject);
    }
}
