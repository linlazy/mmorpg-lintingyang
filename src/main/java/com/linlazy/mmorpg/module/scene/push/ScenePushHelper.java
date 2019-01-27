package com.linlazy.mmorpg.module.scene.push;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.server.common.PushHelper;

import java.util.List;

/**
 * @author linlazy
 */
public class ScenePushHelper {

    public static void pushMonsterDead(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }
    public static void pushSceneEntityDamage(long actorId, List<SceneEntity> data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneEntity-damage",data);
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushEnterScene(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }
}
