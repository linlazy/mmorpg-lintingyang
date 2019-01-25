package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.GuildEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 公会访问类
 * @author linlazy
 */
@Component
@Data
public class GuildDAO extends EntityDAO<GuildEntity> {


     AtomicLong maxGuildId;

    @PostConstruct
    public void init(){
        if(selectMaxGuildId() == null){
            maxGuildId = new AtomicLong(0);
        }else {
            maxGuildId = new AtomicLong(selectMaxGuildId());
        }
    }
    @Override
    protected Class<GuildEntity> forClass() {
        return GuildEntity.class;
    }

    /**
     * 获取公会最大ID
     * @return 返回最大公会ID
     */
    public Long selectMaxGuildId(){
        return jdbcTemplate.queryForObject("select max(guildId) from guild",Long.class);
    }
}
