package com.linlazy.mmorpg.dao;


import com.linlazy.mmorpg.entity.GuildOffLineEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 公会离线消息访问类
 * @author linlazy
 */
@Component
public class GuildOffLineDAO extends EntityDAO<GuildOffLineEntity> {


    @Override
    protected Class<GuildOffLineEntity> forClass() {
        return GuildOffLineEntity.class;
    }

    /**
     * 获取公会接受者的信息集合
     * @param receiver  公会信息接受者
     * @return 返回相应频道上接受者的信息
     */
    public List<GuildOffLineEntity> getReceiveChatSet(long guildId,long receiver){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from guild_offline where guildId = ? and receiver = ?", guildId,receiver);
        return maps.stream()
                .map(map ->{
                    GuildOffLineEntity guildOffLineEntity = new GuildOffLineEntity();
                    guildOffLineEntity.setGuildId((Long) map.get("guildId"));
                    guildOffLineEntity.setReceiverId((Long) map.get("receiverId"));
                    guildOffLineEntity.setSenderId((Long) map.get("senderId"));

                    return guildOffLineEntity;
                })
                .collect(Collectors.toList());
    }
}
