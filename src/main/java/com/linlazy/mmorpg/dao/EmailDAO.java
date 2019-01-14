package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.EmailEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;

/**
 * @author linlazy
 */
public class EmailDAO extends EntityDAO<EmailEntity> {

    @Override
    protected Class<EmailEntity> forClass() {
        return EmailEntity.class;
    }
}
