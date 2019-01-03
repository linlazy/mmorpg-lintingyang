package com.linlazy.mmorpglintingyang.module.chat.service.channel;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpglintingyang.module.chat.constants.ChatType;
import com.linlazy.mmorpglintingyang.module.chat.dao.ChatDao;
import com.linlazy.mmorpglintingyang.module.chat.dto.ChatDTO;
import com.linlazy.mmorpglintingyang.module.chat.entity.Chat;
import com.linlazy.mmorpglintingyang.module.chat.push.ChatPushHelper;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 私聊频道
 * @author linlazy
 */
@Component
public class PrivateChatChannel extends ChatChannel{

    @Autowired
    private ChatDao chatDao;

    /**
     * 订阅事件
     */
    @PostConstruct
    @Override
    public void init(){
        super.init();
        EventBusHolder.register(this);
    }

    /**
     * 关注登录事件
     * @param actorEvent
     */
    @Subscribe
    public void listenEvent(ActorEvent actorEvent){
        //登录时，将私聊信息推送给接受者
        if(actorEvent.getEventType().equals(EventType.LOGIN)){
            Set<Chat> chatSet = chatDao.getReceiveChatSet(actorEvent.getActorId(), ChatType.PRIVATE);
            if(chatSet.size() > 0){
                Set<ChatDTO> chatDTOS = chatSet.stream()
                        .map(ChatDTO::new)
                        .collect(Collectors.toSet());
                ChatPushHelper.pushChatContent(actorEvent.getActorId(),chatDTOS);
            }

            //推送后删除
            chatDao.deleteChatSet(actorEvent.getActorId(),ChatType.PRIVATE);
        }
    }


    @Override
    protected int chatType() {
        return 1;
    }

    @Override
    public Result<?> sendChat(long actorId, JSONObject jsonObject) {

        long targetId = jsonObject.getLongValue("targetId");
        if(targetId == 0){
            return Result.valueOf("参数有误");
        }
        String content = jsonObject.getString("content");


        ChatDTO chatDTO = new ChatDTO();
        //玩家在线
        if(SessionManager.isOnline(targetId)){
            chatDTO.setSourceId(actorId);
            chatDTO.setContent(content);
            chatDTO.setChatType(ChatType.PRIVATE);
            ChatPushHelper.pushChatContent(targetId, Sets.newHashSet(chatDTO));
            return Result.success();
        }else {
            //存档私聊内容
            Chat chat = new Chat();
                Long maxChatId = chatDao.getMaxChatId();
                if(maxChatId == null){
                    maxChatId = 0L;
                }

            chat.setChatId(maxChatId + 1);
            chat.setChatType(ChatType.PRIVATE);
            chat.setSourceId(actorId);
            chat.setContent(content);
            chat.setReceiver(targetId);
            chatDao.addChat(chat);
            return Result.success();
        }
    }
}
