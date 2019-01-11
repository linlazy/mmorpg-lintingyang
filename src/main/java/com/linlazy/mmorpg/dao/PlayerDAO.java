package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.PlayerEntity;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;

/**
 * @author linlazy
 */
@Table("player")
public class PlayerDAO extends EntityDAO<PlayerEntity> {


    /**
     *  通过用户名获取用户信息
     * @param username 用户名
     * @return 返回用户信息
     */
    public PlayerEntity getUserByUsername(String username){
        return queryEntity("select * from user where username = ?",username);
    }

    /**
     * 获取玩家最大ID
     * @return 返回最大玩家ID
     */
    Long getMaxActorId(){
        return jdbcTemplate.queryForObject("select max(actorId) from user",Long.class);
    }


    @Override
    protected Class<PlayerEntity> forClass() {
        return PlayerEntity.class;
    }
}
