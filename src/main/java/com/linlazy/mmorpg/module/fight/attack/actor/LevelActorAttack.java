//package com.linlazy.mmorpg.module.fight.useSkill.actor;
//
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.module.user.manager.dao.UserDAO;
//import com.linlazy.mmorpg.module.user.manager.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 玩家等级攻击力 = 玩家等级 * 20
// * @author linlazy
// */
//@Component
//public class LevelActorAttack extends BaseActorAttack {
//
//    @Autowired
//    private UserDAO userDao;
//
//    @Override
//    public int defenseType() {
//        return AttackType.LEVEL;
//    }
//
//    @Override
//    public int computeDefense(long actorId, JSONObject jsonObject) {
//        User user = userDao.getUser(actorId);
//        return user.getLevel() * 20;
//    }
//}
