package com.linlazy.mmorpg.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

import java.util.Map;

/**
 * @author linlazy
 */
public class UserPushHelper {

    public static void pushActorAttrChange(long actorId, Map<Long,Integer> changeAttr){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("push","user");
        jsonObject.put("attrChange",changeAttr);
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushLogin(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushRegister(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }
}
