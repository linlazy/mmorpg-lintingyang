package com.linlazy.mmorpg.module.copy.fighting.service;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.module.copy.fighting.domain.FightingCopy;
import com.linlazy.mmorpg.module.copy.fighting.event.CopyFightingSuccessEvent;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 副本斗罗之路服务类
 * @author linlazy
 */
@Component
public class CopyFightingService {

    private static Map<Long, FightingCopy> figtingCopyMap = new ConcurrentHashMap<>();

    @Autowired
    private RewardService rewardService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void CopyFightingSuccessEvent(CopyFightingSuccessEvent copyFightingSuccessEvent){
        FightingCopy fightingCopy = copyFightingSuccessEvent.getFightingCopy();
        //发放奖励

        //开启下一层级
        if(fightingCopy.isTriggerNext()){
            fightingCopy.triggerNext();
        }
    }

    private boolean isFightScene(Player player) {
        int fightCopyScene = globalConfigService.getfightCopyScene();
        return player.getSceneId() == fightCopyScene;
    }

}
