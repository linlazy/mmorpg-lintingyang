package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linlazy
 */
@Component
public class ItemDAO extends EntityDAO<ItemEntity> {

    /**
     * 获取玩家道具集合
     * @param actorId 玩家ID
     * @return 返回玩家道具集合
     */
    List<ItemEntity> getItemList(long actorId){
        return jdbcTemplate.queryForList("select * from item where actorId = ?",new Object[]{actorId},ItemEntity.class);
    }

    /**
     * 清空玩家道具
     * @param actorId 玩家ID
     */
    int deleteActorItems(long actorId){
        return jdbcTemplate.update("delete from item where actorId = ?",actorId);
    }

    @Override
    protected Class<ItemEntity> forClass() {
        return ItemEntity.class;
    }
}
