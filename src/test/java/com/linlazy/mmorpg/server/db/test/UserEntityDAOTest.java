package com.linlazy.mmorpg.server.db.test;

import com.linlazy.mmorpg.server.db.EntityOperatorType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author linlazy
 */

public class UserEntityDAOTest {

    @Autowired
    private UserEntityDao userEntityDao;

    @Test
    public void updateQueue() {
        UserEntity user = new UserEntity();
        user.setOperatorType(EntityOperatorType.INSERT);
        user.setActorId(4194306);
        user.setUsername("linlazy");
        userEntityDao.updateQueue(user);
    }
}