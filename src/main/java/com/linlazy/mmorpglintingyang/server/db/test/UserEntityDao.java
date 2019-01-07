package com.linlazy.mmorpglintingyang.server.db.test;

import com.linlazy.mmorpglintingyang.server.db.Entity;
import com.linlazy.mmorpglintingyang.server.db.EntityDao;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */

@Component
public class UserEntityDao extends EntityDao {

    @Override
    public void updateQueue(Entity entity) {
        super.updateQueue(entity);
    }
}
