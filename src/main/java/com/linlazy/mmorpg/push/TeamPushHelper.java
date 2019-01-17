package com.linlazy.mmorpg.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

/**
 * @author linlazy
 */
public class TeamPushHelper {

    public static void pushTeam(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("code", message);
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushTeamCaption(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", message);
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushLeaveTeam(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", message);
        PushHelper.push(actorId,jsonObject);
    }

}
