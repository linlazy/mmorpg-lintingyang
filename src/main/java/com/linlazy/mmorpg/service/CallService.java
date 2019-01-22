package com.linlazy.mmorpg.service;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.*;
import com.linlazy.mmorpg.event.type.PlayerCallDisappearEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.push.PlayerCallPushHelper;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
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

    @Autowired
    private SkillService skillService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private PlayerService playerService;

    @PostConstruct
    public void  init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void playerCallDisappear(PlayerCallDisappearEvent playerCallDisappearEvent){
        PlayerCall playerCall = playerCallDisappearEvent.getPlayerCall();
        playerCall.getAutoAttackScheduledFuture().cancel(true);
        callMap.remove(playerCall.getId());
        Scene scene = sceneService.getSceneBySceneEntity(playerCall);
        scene.getPlayerCallMap().remove(playerCall.getId());

        Player player = playerService.getPlayer(playerCall.getSourceId());
        player.setPlayerCall(null);
        PlayerCallPushHelper.pushDisappear(playerCall.getSourceId(),String.format("你的召唤兽【%s】到达时间后消失",playerCall.getName()));
    }

    public PlayerCall createCall(SceneEntity sceneEntity, int continueTime){
        Player player = (Player) sceneEntity;
        PlayerCall playerCall = new PlayerCall(player);

        List<Skill> skillList = skillService.getPlayerCallSkillList();
        playerCall.setSkillList(skillList);
        playerCall.setName("深渊巨兽");
        playerCall.setSceneEntityType(SceneEntityType.PLAYER_CALL);
        playerCall.setSceneId(player.getSceneId());
        playerCall.setCopyId(player.getCopyId());

        playerCall.setId(maxPlayerCallId.incrementAndGet());

        playerCall.startPlayerAutoAttackScheduled();
        playerCall.startPlayerCallDisAppearScheduled(continueTime);

        callMap.put(maxPlayerCallId.get(),playerCall);
        SceneService sceneService = SpringContextUtil.getApplicationContext().getBean(SceneService.class);
        Scene scene = sceneService.getSceneBySceneEntity(sceneEntity);
        scene.getPlayerCallMap().put(playerCall.getId(),playerCall);

        player.setPlayerCall(playerCall);
        return playerCall;
    }

}
