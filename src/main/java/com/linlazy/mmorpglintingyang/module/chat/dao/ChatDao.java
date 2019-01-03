package com.linlazy.mmorpglintingyang.module.chat.dao;


import com.linlazy.mmorpglintingyang.module.chat.entity.Chat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @author linlazy
 */
@Mapper
public interface ChatDao {


    /**
     * 增加聊天信息
     * @param chat 聊天信息
     */
    @Insert("insert into chat (chatId,sourceId,content,receiver,chatType)values(#{chatId},#{sourceId},#{content},#{receiver},#{chatType})")
    void addChat(Chat chat);

    /**
     * 获取接受聊天信息者的信息集合
     * @param receiver  聊天信息接受者
     * @param chatType 聊天频道
     * @return 返回相应频道上接受者的聊天信息
     */
    @Select("select * from chat where receiver = #{receiver} and chatType = #{chatType}")
    Set<Chat> getReceiveChatSet(long receiver,int chatType);

    /**
     * 获取最大聊天ID
     * @return  返回最大聊天ID
     */
    @Select("select max(chatId) from chat")
    Long getMaxChatId();

    /**
     * 删除接受者的相应频道上的聊天信息
     * @param receiver  接受者ID
     * @param chatType 聊天频道
     */
    @Select("delete from chat where receiver = #{receiver} and chatType = #{chatType}")
    void deleteChatSet(long receiver, int chatType);
}
