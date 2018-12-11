package com.linlazy.mmorpglintingyang.module.scene.handler;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.scene.service.SceneService;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Module("scene")
public class SceneHandler {


    @Autowired
    private SceneService sceneService;

    /**
     * 获取场景信息
     * @param jsonObject
     * @return
     */
    @Cmd("getAllInfo")
    public Result<?> info(JSONObject jsonObject){
        return sceneService.getAllInfo();
    }


    /**
     * 移动场景
     * @param jsonObject
     * @return
     */
    @Cmd("move")
    public Result<?> move(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        int targetSceneId = jsonObject.getIntValue("target_scene_id");
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

    /**
     * 获取当前场景的实体信息
     * @param jsonObject
     * @return
     */
    @Cmd("getCurrentInfo")
    public Result<?> getCurrentInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return sceneService.getCurrentInfo(actorId);
    }

//    /**
//     * 对话NPC
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("talkNPC")
//    public Result<?> talkNPC(JSONObject jsonObject){
//        long actorId = jsonObject.getLong("actorId");
//        return sceneService.talkNPC(actorId);
//    }
}
