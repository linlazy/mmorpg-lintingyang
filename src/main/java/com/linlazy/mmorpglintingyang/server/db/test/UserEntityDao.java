package com.linlazy.mmorpglintingyang.server.db.test;

import com.linlazy.mmorpglintingyang.server.db.EntityDao;
import com.linlazy.mmorpglintingyang.server.db.Identity;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */

@Component
public class UserEntityDao extends EntityDao<UserEntity> {

    @Override
    protected Class<UserEntity> forClass() {
        return UserEntity.class;
    }

    public UserEntity getUserEntity(long actorId) {
        return super.getEntity( Identity.build(actorId));
    }
}
