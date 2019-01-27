package com.linlazy.mmorpg.module.equip.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

/**
 * @author linlazy
 */
public class EquipPushHelper {

    /**
     * 推送已穿戴装备信息
     * @param actorId
     * @param message
     */
    public static void pushDressedEquip(long actorId,String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

}
