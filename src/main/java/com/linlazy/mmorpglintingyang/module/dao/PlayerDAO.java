package com.linlazy.mmorpglintingyang.module.dao;

import com.linlazy.mmorpglintingyang.module.entity.PlayerEntity;
import com.linlazy.mmorpglintingyang.server.db.EntityDao;
import com.linlazy.mmorpglintingyang.server.db.Identity;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class PlayerDAO extends EntityDao<PlayerEntity> {

    /**
     * 通过玩家标识获取玩家实体
     * @param actorId 玩家ID
     * @return 返回玩家实体
     */
    public  PlayerEntity getPlayerEntity(long actorId) {
        return  getEntity(Identity.build(actorId));
    }


    /**
     * 通过玩家用户名获取玩家实体
     * @param username 用户名
     * @return 返回玩家实体
     */
    public  PlayerEntity getPlayerEntityByUsername(String username) {
        return  getEntity(Identity.build(username));
    }

    @Override
    protected Class<PlayerEntity> forClass() {
        return PlayerEntity.class;
    }
}
