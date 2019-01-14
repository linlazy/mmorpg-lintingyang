//package com.linlazy.mmorpg.module.fight.handler;
//
//import com.alibaba.fastjson.JSONObject;
////import com.linlazy.mmorpg.module.fight.service.FightService;
//import com.linlazy.mmorpg.server.common.Result;
//import com.linlazy.mmorpg.server.route.Cmd;
//import com.linlazy.mmorpg.server.route.Module;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author linlazy
// */
//@Module("fight")
//@Component
//public class FightHandler {
//
//    @Autowired
//    private FightService fightService;
//
//    /**
//     * 攻击
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("attack")
//    public Result<?> attack(JSONObject jsonObject){
//        long actorId = jsonObject.getLongValue("actorId");
//        return fightService.attack(actorId,jsonObject);
//    }
//
//}
