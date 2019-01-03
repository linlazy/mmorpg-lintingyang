package com.linlazy.mmorpglintingyang.module.fight.attack.actor;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 玩家等级攻击力 = 玩家等级 * 20
 * @author linlazy
 */
@Component
public class LevelActorAttack extends ActorAttack {

    @Autowired
    private UserDao userDao;

    @Override
    public int attackType() {
        return AttackType.LEVEL;
    }

    @Override
    public int computeAttack(long actorId, JSONObject jsonObject) {
        User user = userDao.getUser(actorId);
        return user.getLevel() * 20;
    }
}
