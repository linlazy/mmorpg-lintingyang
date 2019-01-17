package com.linlazy.mmorpg.event.handler;


import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.event.GameEvenHandler;
import com.linlazy.mmorpg.event.type.PlayerAttackEvent;
import com.linlazy.mmorpg.event.type.PlayerDeadEvent;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.push.PlayerPushHelper;
import com.linlazy.mmorpg.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author linlazy
 */

@Component
public class ActorEventHandler extends GameEvenHandler {

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }


    @Autowired
    private PlayerService playerService;

    @Autowired
    private SceneConfigService sceneConfigService;

    /**
     * 玩家死亡
     * @param playerDeadEvent
     */
    @Subscribe
    public void playerDead(PlayerDeadEvent playerDeadEvent){
        Player deadPlayer = playerDeadEvent.getPlayer();
        //广播通知与死亡玩家 同场景玩家
        Map<Long, Player> sameScenePlayerMap = playerService.getSameScenePlayerMap(deadPlayer.getActorId());
        sameScenePlayerMap.values().stream()
//                .filter(sameScenePlayer -> sameScenePlayer.getActorId() != deadPlayer.getActorId())
                .forEach(sameScenePlayer -> {
                    PlayerPushHelper.pushPlayerDead(sameScenePlayer.getActorId(),String.format("玩家【%s】已死亡",deadPlayer.getName()));
                });

    }

    /**
     * 玩家攻击事件
     * @param playerAttackEvent
     */
    @Subscribe
    public void playerAttack(PlayerAttackEvent playerAttackEvent){
        Player player = playerAttackEvent.getPlayer();
        player.getPlayerCall().active();

    }
}
