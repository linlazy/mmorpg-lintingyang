package com.linlazy.mmorpglintingyang.module.guild.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.PushHelper;

/**
 * @author linlazy
 */
public class GuildPushHelper {




    public static void pushGuildOperator(long actorId,int operatorType, JSONObject jsonObject){

        PushHelper.push(actorId,jsonObject);
    }
}
