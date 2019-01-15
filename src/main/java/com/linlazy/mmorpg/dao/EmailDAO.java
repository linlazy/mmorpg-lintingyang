package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.EmailEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class EmailDAO extends EntityDAO<EmailEntity> {

    @Override
    protected Class<EmailEntity> forClass() {
        return EmailEntity.class;
    }
}
