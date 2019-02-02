package com.linlazy.mmorpg.module.item.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

/**
 * @author linlazy
 */
public class ItemPushHelper {

    /**
     * 推送信息装备
     * @param actorId
     * @param message
     */
    public static void pushitemInfo(long actorId,String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

}
