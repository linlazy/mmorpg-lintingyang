package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<ItemEntity> getItemList(long actorId){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from item where actorId = ?", new Object[]{actorId});
        return maps.stream()
                .map(map-> {
                    ItemEntity itemEntity = new ItemEntity();
                    itemEntity.setItemId((Long) map.get("itemId"));
                    itemEntity.setActorId((Long) map.get("actorId"));
                    itemEntity.setExt((String) map.get("ext"));
                    itemEntity.setCount((Integer) map.get("count"));
                    itemEntity.afterReadDB();
                    return itemEntity;
                })
                .collect(Collectors.toList());
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
