package com.linlazy.mmorpglintingyang.module.chat.dao;


import com.linlazy.mmorpglintingyang.module.chat.entity.Chat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface ChatDao {


    @Insert("insert into chat (chatId,sourceId,content,receiver)values(#{chatId},#{sourceId},#{content},#{receiver})")
    void addChat(Chat chat);

    @Select("select * from chat where receiver = #{receiver} and chatType = #{chatType}")
    Set<Chat> getReceiveChatSet(long receiver,int chatType);

    @Select("select max(chatId) from chat")
    Long getMaxChatId();
}
