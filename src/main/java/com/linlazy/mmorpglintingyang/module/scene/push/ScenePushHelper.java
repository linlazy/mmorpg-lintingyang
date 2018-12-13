package com.linlazy.mmorpglintingyang.module.scene.push;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.PushHelper;
import com.linlazy.mmorpglintingyang.module.scene.manager.entity.model.SceneEntityInfo;

public class ScenePushHelper {

    public static void pushMonster(long actorId, SceneEntityInfo data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("push","scene");
        jsonObject.put("monster",data);
        PushHelper.push(actorId,jsonObject);
    }
}
