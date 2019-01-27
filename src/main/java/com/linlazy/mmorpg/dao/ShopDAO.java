package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.ShopEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    public List<ShopEntity> getPlayerShopEntity(long actorId){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from shop where actorId = ?", actorId);
        return maps.stream()
                .map(map->{
                    ShopEntity shopEntity = new ShopEntity();

                    shopEntity.setActorId((Long) map.get("actorId"));
                    shopEntity.setGoodsId((Long) map.get("goodsId"));
                    shopEntity.setHasBuyTimes((Integer) map.get("hasBuyTimes"));
                    shopEntity.setNextResetTime((Long) map.get("nextResetTime"));

                    return shopEntity;
                }).collect(Collectors.toList());
    }

}
