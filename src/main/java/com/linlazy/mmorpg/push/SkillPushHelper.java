package com.linlazy.mmorpg.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

/**
 * @author linlazy
 */
public class SkillPushHelper {

    public static void pushUseSkill(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }
}
