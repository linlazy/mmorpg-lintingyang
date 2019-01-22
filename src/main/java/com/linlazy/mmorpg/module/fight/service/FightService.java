//package com.linlazy.mmorpg.module.fight.service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.module.fight.service.attackmode.BaseAttackMode;
//import com.linlazy.mmorpg.server.common.Result;
//import org.springframework.stereotype.Component;
//
///**
// * @author linlazy
// */
//@Component
//public class FightService {
//
//    public Result<?> useSkill(long actorId, JSONObject jsonObject) {
//        int attackMode = jsonObject.getIntValue("attackMode");
//        return BaseAttackMode.getAttackMode(attackMode).useSkill(actorId,jsonObject);
//    }
//}
