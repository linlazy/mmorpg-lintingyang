package com.linlazy.mmorpg.module.scene.copy.push;


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

    /**
     * 副本成功
     * @param actorId
     * @param message
     */
    public static void pushCopySuccess(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

    /**
     * 副本小怪刷新
     * @param actorId
     * @param message
     */
    public static void pushCopyMonsterRefresh(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

    /**
     * 副本小怪刷新
     * @param actorId
     * @param message
     */
    public static void pushCopyBossRefresh(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }
}
