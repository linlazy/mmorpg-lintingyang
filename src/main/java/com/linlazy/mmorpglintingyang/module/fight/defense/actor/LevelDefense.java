package com.linlazy.mmorpglintingyang.module.fight.defense.actor;

import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 等级防御力 = 等级 * 19
 */
@Component
public class LevelDefense extends Defense {

    @Autowired
    private UserDao userDao;

    @Override
    public int defenseType() {
        return DefenseType.LEVEL;
    }

    @Override
    public int computeDefense(long actorId) {
        User user = userDao.getUser(actorId);
        return user.getLevel() * 19;
    }
}
