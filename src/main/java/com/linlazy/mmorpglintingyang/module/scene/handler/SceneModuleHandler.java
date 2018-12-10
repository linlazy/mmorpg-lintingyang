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
public class SceneModuleHandler{


    @Autowired
    private SceneService sceneService;

    /**
     * 获取场景信息
     * @param jsonObject
     * @return
     */
    @Cmd("info")
    public Result<?> info(JSONObject jsonObject){
        return sceneService.info();
    }


    /**
     * 移动场景
     * @param jsonObject
     * @return
     */
    @Cmd("move")
    public Result<?> move(JSONObject jsonObject){
        int targetSceneId = jsonObject.getIntValue("target_scene_id");
        return sceneService.moveTo(targetSceneId);
    }
}
