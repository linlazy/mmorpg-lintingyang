//package com.linlazy.mmorpg.module.fight.service.canattacked;
//
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.module.fight.validator.FightValidtor;
//import com.linlazy.mmorpg.constants.SceneEntityType;
//import com.linlazy.mmorpg.server.common.Result;
//import com.linlazy.mmorpg.utils.SessionManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author linlazy
// */
//@Component
//public class ActorCanAttacked extends BaseCanAttacked {
//    @Override
//    protected int entityType() {
//        return SceneEntityType.PLAYER;
//    }
//
//    @Autowired
//    private FightValidtor fightValidtor;
//
//    @Override
//    public Result<?> canAttacked(long actorId, JSONObject jsonObject) {
//        long entityId = jsonObject.getLongValue("entityId");
//        if(fightValidtor.isDifferentScene(actorId,entityId)){
//                return Result.valueOf("处于不同场景");
//        }
//        if(!SessionManager.isOnline(entityId)){
//            return Result.valueOf("被攻击玩家不在线");
//        }
//        return Result.success();
//    }
//}
