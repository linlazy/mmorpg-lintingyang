package com.linlazy.mmorpglintingyang.module.pk.arena.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.pk.arena.manager.ArenaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author linlazy
 */
@Component
public class ArenaService {

    @Autowired
    private ArenaManager arenaManager;


    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }


    @Subscribe
    public void listenEvent(ActorEvent actorEvent){

        switch (actorEvent.getEventType()){
            case ARENA_ACTOR_DEAD:
                handleArenaActorKilled(actorEvent);
                break;
                default:
                    break;
        }
    }


    /**
     * 处理竞技场玩家击杀事件
     * @param actorEvent
     */
    private void handleArenaActorKilled(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject) actorEvent.getData();
        int arenaId = jsonObject.getIntValue("arenaId");
        int killId = jsonObject.getIntValue("killId");
        int killedId = jsonObject.getIntValue("killedId");

        arenaManager.handleArenaActorKilled(arenaId,killId,killedId);
    }
}
