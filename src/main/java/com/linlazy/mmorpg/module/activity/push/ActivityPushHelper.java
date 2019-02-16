package com.linlazy.mmorpg.module.activity.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

/**
 * 活动推送
 * @author linlazy
 */
public class ActivityPushHelper {

    public static void pushStart(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", message);
        PushHelper.push(actorId, jsonObject);
    }

}
