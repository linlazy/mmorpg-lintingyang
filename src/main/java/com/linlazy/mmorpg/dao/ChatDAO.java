package com.linlazy.mmorpg.dao;


import com.linlazy.mmorpg.entity.ChatEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Component
public class ChatDAO extends EntityDAO<ChatEntity> {



    /**
     * 获取接受聊天信息者的信息集合
     * @param receiver  聊天信息接受者
     * @return 返回相应频道上接受者的聊天信息
     */
    public List<ChatEntity> getReceiveChatSet(long receiver){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from chat where receiver = ?", receiver);
        return maps.stream()
                .map(map ->{
                    ChatEntity chatEntity = new ChatEntity();
                    chatEntity.setChatId((Long) map.get("chatId"));
                    chatEntity.setReceiver((Long) map.get("receiver"));
                    chatEntity.setSourceId((Long) map.get("senderId"));
                    chatEntity.setContent((String) map.get("content"));

                    return chatEntity;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取最大聊天ID
     * @return  返回最大聊天ID
     */
    public Long getMaxChatId(){
         return jdbcTemplate.queryForObject("select max(chatId) from chat",Long.class);
    }

    /**
     * 删除接受者的相应频道上的聊天信息
     * @param receiver  接受者ID
     */
    public void deleteChatSet(long receiver){
        jdbcTemplate.update("delete from chat where receiver = ?",receiver);
    }

    @Override
    protected Class<ChatEntity> forClass() {
        return ChatEntity.class;
    }
}
