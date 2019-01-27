package com.linlazy.mmorpg.module.email.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

/**
 * 邮件推送类
 * @author linlazy
 */
public class EmailPushHelper {

    public static void pushEmail(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", message);
        PushHelper.push(actorId, jsonObject);
    }

}
