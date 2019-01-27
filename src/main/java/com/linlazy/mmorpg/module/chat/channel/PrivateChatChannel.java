package com.linlazy.mmorpg.module.chat.channel;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.module.chat.constants.ChatType;
import com.linlazy.mmorpg.dao.ChatDAO;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.chat.dto.ChatDTO;
import com.linlazy.mmorpg.entity.ChatEntity;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.chat.push.ChatPushHelper;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 私聊频道
 * @author linlazy
 */
@Component
public class PrivateChatChannel extends BaseChatChannel {

    @Autowired
    private ChatDAO chatDao;
    @Autowired
    private PlayerService playerService;

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
            List<ChatEntity> chatSet = chatDao.getReceiveChatSet(actorEvent.getActorId());
            if(chatSet.size() > 0){
                chatSet.stream()
                        .map(ChatDTO::new)
                        .forEach(chatDTO -> {
                            ChatPushHelper.pushPrivateChatContent(actorEvent.getActorId(),chatDTO.toString());
                        });
            }

            //推送后删除
            chatDao.deleteChatSet(actorEvent.getActorId());
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

            Player player = playerService.getPlayer(actorId);
            ChatPushHelper.pushPrivateChatContent(targetId, String.format("玩家【%s】对你说：【%s】",player.getName(),content));
            return Result.success();
        }else {
            //存档私聊内容
            ChatEntity chat = new ChatEntity();
                Long maxChatId = chatDao.getMaxChatId();
                if(maxChatId == null){
                    maxChatId = 0L;
                }

            chat.setChatId(maxChatId + 1);
            chat.setSourceId(actorId);
            chat.setContent(content);
            chat.setReceiver(targetId);
            chatDao.insertQueue(chat);
            return Result.success();
        }
    }
}
