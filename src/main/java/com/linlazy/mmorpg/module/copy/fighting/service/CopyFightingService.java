package com.linlazy.mmorpg.module.copy.fighting.service;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.module.copy.fighting.domain.FightingCopy;
import com.linlazy.mmorpg.module.copy.fighting.event.CopyFightingBossDeadEvent;
import com.linlazy.mmorpg.module.copy.fighting.event.CopyFightingUseSkillEvent;
import com.linlazy.mmorpg.module.copy.fighting.trigger.CopyFightTrigger;
import com.linlazy.mmorpg.module.scene.domain.Boss;
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

    /**
     * 使用技能
     * @param copyFightingUseSkillEvent
     */
    @Subscribe
    public void copyFightingUseSkillEvent(CopyFightingUseSkillEvent copyFightingUseSkillEvent){
        FightingCopy fightingCopy = copyFightingUseSkillEvent.getFightingCopy();
        fightingCopy.getPlayerFightingCopyProcess().setUseSkill(true);
    }


    @Subscribe
    public void copyFightingBossDeadEvent(CopyFightingBossDeadEvent copyFightingBossDeadEvent){
        FightingCopy fightingCopy = copyFightingBossDeadEvent.getFightingCopy();
        //发放奖励
        fightingCopy.getPlayerMap().values()
                .stream()
                .forEach(player -> {
                    Boss boss = fightingCopy.getBossList().get(fightingCopy.getCopyLevel()-1);
                    rewardService.addRewardList(player.getActorId(),Lists.newArrayList(boss.getReward()));
                });

        //开启下一层级
        CopyFightTrigger copyFightTrigger = CopyFightTrigger.getCopyFightTrigger(fightingCopy.getCopyLevel());
        if(copyFightTrigger != null){
            if(copyFightTrigger.isTriggerNext(fightingCopy)){
                copyFightTrigger.doTriggerNext(fightingCopy);
            }
        }
    }



}
