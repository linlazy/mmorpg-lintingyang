package com.linlazy.mmorpg.module.guild.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

/**
 * @author linlazy
 */
public class GuildPushHelper {




    public static void pushGuildOperator(long actorId,int operatorType, JSONObject jsonObject){

        PushHelper.push(actorId,jsonObject);
    }
}
