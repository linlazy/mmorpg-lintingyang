package com.linlazy.mmorpglintingyang.module.fight.defense.actor;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 等级防御力 = 等级 * 19
 * @author linlazy
 */
@Component
public class LevelActorDefense extends BaseActorDefense {

    @Autowired
    private UserDao userDao;

    @Override
    public int actorDefenseType() {
        return ActorDefenseType.LEVEL;
    }

    @Override
    public int computeDefense(long actorId, JSONObject jsonObject) {
        User user = userDao.getUser(actorId);
        return user.getLevel() * 19;
    }
}
