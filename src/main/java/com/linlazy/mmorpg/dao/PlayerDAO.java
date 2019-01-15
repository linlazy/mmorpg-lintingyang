package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.PlayerEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author linlazy
 */
@Component
@Data
public class PlayerDAO extends EntityDAO<PlayerEntity> {

    private AtomicLong maxActorId;


    @PostConstruct
    public void init(){
        if(selectMaxActorId() == null){
            maxActorId = new AtomicLong(0);
        }else {
            maxActorId = new AtomicLong(selectMaxActorId());
        }
    }

    /**
     *  通过用户名获取用户信息
     * @param username 用户名
     * @return 返回用户信息
     */
    public PlayerEntity getUserByUsername(String username){
        return queryEntity("select * from player where username = ?",username);
    }

    /**
     * 获取玩家最大ID
     * @return 返回最大玩家ID
     */
    private Long selectMaxActorId(){
        return jdbcTemplate.queryForObject("select max(actorId) from player",Long.class);
    }


    @Override
    protected Class<PlayerEntity> forClass() {
        return PlayerEntity.class;
    }
}
