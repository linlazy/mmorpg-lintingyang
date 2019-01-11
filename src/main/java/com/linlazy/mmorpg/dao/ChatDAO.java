package com.linlazy.mmorpg.dao;


import com.linlazy.mmorpg.entity.ChatEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;

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
    List<ChatEntity> getReceiveChatSet(long receiver){
        return jdbcTemplate.queryForList("select * from chat where receiver = #{receiver}",new  Object[]{receiver},ChatEntity.class);
    }

    /**
     * 获取最大聊天ID
     * @return  返回最大聊天ID
     */
    Long getMaxChatId(){
         return jdbcTemplate.queryForObject("select max(chatId) from chat",Long.class);
    }

    /**
     * 删除接受者的相应频道上的聊天信息
     * @param receiver  接受者ID
     */
    void deleteChatSet(long receiver){
        jdbcTemplate.update("delete from chat where receiver = ?",receiver);
    }

    @Override
    protected Class<ChatEntity> forClass() {
        return ChatEntity.class;
    }
}
