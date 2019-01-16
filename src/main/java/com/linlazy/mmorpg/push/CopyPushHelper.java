package com.linlazy.mmorpg.push;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

/**
 * @author linlazy
 */
public class CopyPushHelper {
    public static void pushEnterSuccess(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

    /**
     * 副本失败
     * @param actorId
     * @param message
     */
    public static void pushCopyFail(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

}
