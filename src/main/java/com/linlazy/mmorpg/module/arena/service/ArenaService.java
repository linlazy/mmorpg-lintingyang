package com.linlazy.mmorpg.module.arena.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.dao.ArenaDAO;
import com.linlazy.mmorpg.module.arena.domain.Arena;
import com.linlazy.mmorpg.entity.ArenaEntity;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author linlazy
 */
@Component
public class ArenaService {

    @Autowired
    private ArenaDAO arenaDao;

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

        //击杀玩家得100分,击杀数加一
        ArenaEntity killArena = arenaDao.getEntityByPK(arenaId,killId);
        if(killArena == null){
            killArena =new ArenaEntity();
            killArena.setActorId(killId);
            killArena.setArenaId(arenaId);
            arenaDao.insertQueue(killArena);
        }
        Arena arena = new Arena();
        arena.modifyScore(100);
        arena.increaseKillNum();
        arenaDao.updateQueue(killArena);

        //被击玩家杀扣50分,被击杀数加一
        ArenaEntity killedArenaEntity = arenaDao.getEntityByPK(arenaId,killedId);
        if(killedArenaEntity == null){
            killedArenaEntity =new ArenaEntity();
            killedArenaEntity.setActorId(killedId);
            killedArenaEntity.setArenaId(arenaId);
        }
        Arena killedArena = new Arena();


        killedArena.modifyScore(-50);
        killedArena.increaseKilledNum();
        arenaDao.updateQueue(killedArena.convertArenaEntity());
    }
}
