package com.linlazy.mmorpglintingyang.module.scene.push;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.PushHelper;

public class ScenePushHelper {

    public void pushMonster(long actorId,String data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("push","scene");
        PushHelper.push(actorId,jsonObject);
    }
}
