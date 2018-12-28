package com.linlazy.mmorpglintingyang.module.scene.push;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.server.common.PushHelper;

import java.util.List;

public class ScenePushHelper {

    public static void pushMonster(long actorId, List<SceneEntityDo> data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("monster-dead",data);
        PushHelper.push(actorId,jsonObject);
    }
    public static void pushSceneEntityDamage(long actorId, List<SceneEntityDo> data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneEntity-damage",data);
        PushHelper.push(actorId,jsonObject);
    }
}
