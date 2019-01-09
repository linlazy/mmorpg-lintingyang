package com.linlazy.mmorpglintingyang.module.fight.validator;

import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class FightValidtor {

    @Autowired
    private UserDAO userDao;

    public boolean isDifferentScene(long actorId, long  targetId){
        return userDao.getUser(actorId).getSceneId() != userDao.getUser(targetId).getSceneId();
    }
}
