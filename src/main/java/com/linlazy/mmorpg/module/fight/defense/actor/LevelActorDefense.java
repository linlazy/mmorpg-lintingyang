package com.linlazy.mmorpg.module.fight.defense.actor;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.user.manager.dao.UserDAO;
import com.linlazy.mmorpg.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 等级防御力 = 等级 * 19
 * @author linlazy
 */
@Component
public class LevelActorDefense extends BaseActorDefense {

    @Autowired
    private UserDAO userDao;

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
