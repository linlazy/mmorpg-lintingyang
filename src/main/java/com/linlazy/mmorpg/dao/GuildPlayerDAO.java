package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.GuildPlayerEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Component
public class GuildPlayerDAO extends EntityDAO<GuildPlayerEntity> {




    /**
     * 获取所有公会玩家信息
     * @return 返回公会所有玩家信息
     */
    public List<GuildPlayerEntity> getGuildPlayerList(){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from guild_player");
        return maps.stream()
                .map(map->{
                    GuildPlayerEntity guildPlayerEntity = new GuildPlayerEntity();

                    guildPlayerEntity.setGuildId((Long) map.get("guildId"));
                    guildPlayerEntity.setActorId((Long) map.get("actorId"));
                    guildPlayerEntity.setAuthLevel((Integer) map.get("authLevel"));

                    return guildPlayerEntity;
                })
                .collect(Collectors.toList());
    }

    @Override
    protected Class<GuildPlayerEntity> forClass() {
        return GuildPlayerEntity.class;
    }

}
