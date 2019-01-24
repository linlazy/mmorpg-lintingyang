package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.ShopEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

/**
 *
 * @author linlazy
 */
@Component
public class ShopDAO extends EntityDAO<ShopEntity> {
    @Override
    protected Class<ShopEntity> forClass() {
        return ShopEntity.class;
    }



}
