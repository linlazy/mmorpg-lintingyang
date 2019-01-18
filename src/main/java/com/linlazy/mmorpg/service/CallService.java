package com.linlazy.mmorpg.service;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerCall;
import com.linlazy.mmorpg.domain.Scene;
import com.linlazy.mmorpg.event.type.PlayerCallDisappearEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 召唤兽服务
 * @author linlazy
 */
@Component
public class CallService {

    private Map<Long,PlayerCall> callMap = new ConcurrentHashMap<>();

    private AtomicLong maxPlayerCallId = new AtomicLong();


    @PostConstruct
    public void  init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void playerCallDisappear(PlayerCallDisappearEvent playerCallDisappearEvent){
        PlayerCall playerCall = playerCallDisappearEvent.getPlayerCall();
        playerCall.getScheduledFuture().cancel(true);
        callMap.remove(playerCall.getId());
    }

    public PlayerCall createCall(SceneEntity sceneEntity, int continueTime){
        Player player = (Player) sceneEntity;
        PlayerCall playerCall = new PlayerCall(player);

        playerCall.setId(maxPlayerCallId.incrementAndGet());

        playerCall.startPlayerCallDisAppearScheduled(continueTime);

        callMap.put(maxPlayerCallId.get(),playerCall);
        SceneService sceneService = SpringContextUtil.getApplicationContext().getBean(SceneService.class);
        Scene scene = sceneService.getSceneBySceneEntity(sceneEntity);
        scene.getPlayerCallMap().put(playerCall.getId(),playerCall);
        return playerCall;
    }

}
