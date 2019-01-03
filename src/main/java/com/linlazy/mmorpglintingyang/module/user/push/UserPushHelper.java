package com.linlazy.mmorpglintingyang.module.user.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.PushHelper;

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
}
