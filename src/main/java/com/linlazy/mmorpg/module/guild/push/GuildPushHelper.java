package com.linlazy.mmorpg.module.guild.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

/**
 * @author linlazy
 */
public class GuildPushHelper {

    public static void pushGuild(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }
}
