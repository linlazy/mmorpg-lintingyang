package com.linlazy.mmorpg.service;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerCall;
import com.linlazy.mmorpg.event.type.PlayerCallDisappearEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
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

    public void createCall(SceneEntity sceneEntity, int continueTime){
        Player player = (Player) sceneEntity;
        PlayerCall playerCall = new PlayerCall(player);

        playerCall.setId(maxPlayerCallId.incrementAndGet());

        playerCall.startPlayerCallDisAppearScheduled(continueTime);

        callMap.put(maxPlayerCallId.get(),playerCall);
    }

}
