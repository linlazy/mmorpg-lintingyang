package com.linlazy.mmorpg.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.PlayerSkill;
import com.linlazy.mmorpg.dto.PlayerDTO;
import com.linlazy.mmorpg.server.common.PushHelper;

import java.util.Map;

/**
 * @author linlazy
 */
public class PlayerPushHelper {

    public static void pushActorAttrChange(long actorId, Map<Long,Integer> changeAttr){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("push","user");
        jsonObject.put("attrChange",changeAttr);
        PushHelper.push(actorId,jsonObject);
    }


    public static void pushChange(long actorId, PlayerDTO playerDTO){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",playerDTO.toString());
        PushHelper.push(actorId,jsonObject);
    }
    public static void pushAttacked(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushLogin(long actorId, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushRegister(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushPlayerDead(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushPlayerInfo(long actorId,PlayerDTO playerDTO){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",playerDTO.toString());
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushPlayerSkillInfo(long actorId, PlayerSkill playerSkill){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",playerSkill.toString());
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushAttack(long actorId, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",message);
        PushHelper.push(actorId,jsonObject);
    }
}
