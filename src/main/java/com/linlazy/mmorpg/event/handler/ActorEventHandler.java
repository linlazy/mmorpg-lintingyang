package com.linlazy.mmorpg.event.handler;


import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.event.GameEvenHandler;
import com.linlazy.mmorpg.event.type.CopyPlayerDeadEvent;
import com.linlazy.mmorpg.event.type.PlayerAttackEvent;
import com.linlazy.mmorpg.event.type.PlayerDeadEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.scene.config.SceneConfigService;
import com.linlazy.mmorpg.push.PlayerPushHelper;
import com.linlazy.mmorpg.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

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
        Set<Player> sameScenePlayerSet = playerService.getSameScenePlayerSet(deadPlayer.getActorId());
        sameScenePlayerSet.stream()
                .filter(sameScenePlayer -> sameScenePlayer.getActorId() != deadPlayer.getActorId())
                .forEach(sameScenePlayer -> {
                    PlayerPushHelper.pushPlayerDead(deadPlayer.getActorId(),String.format("玩家【%s】已死亡",deadPlayer.getName()));
                });

        //如果是副本，触发副本玩家死亡事件
        if(sceneConfigService.isCopyScene(deadPlayer.getSceneId())){
            EventBusHolder.register(new CopyPlayerDeadEvent(deadPlayer));
        }
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
