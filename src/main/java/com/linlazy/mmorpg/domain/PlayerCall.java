package com.linlazy.mmorpg.domain;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.event.type.PlayerAttackEvent;
import com.linlazy.mmorpg.event.type.PlayerAttackedEvent;
import com.linlazy.mmorpg.event.type.PlayerCallDisappearEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.service.SkillService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 玩家召唤兽
 * @author linlazy
 */
@Data
public class PlayerCall extends SceneEntity {

    private long id;

    private long sourceId;

    private PlayerCallSkill playerCallSkill;

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> scheduledFuture;

    /**
     * 激活
     */
    private volatile boolean active;

    public PlayerCall(Player player) {
        sourceId = player.getActorId();

        EventBusHolder.register(this);
        startPlayerAutoAttackScheduled();
    }

    @Subscribe
    public void playerAttackEvent(PlayerAttackEvent playerAttackEvent){
        if(playerAttackEvent.getPlayer().getActorId() == sourceId){
            active =true;
        }
    }


    @Subscribe
    public void playerAttackedEvent(PlayerAttackedEvent playerAttackedEvent){
        if(playerAttackedEvent.getPlayer().getActorId() == sourceId){
            active =true;
        }
    }

    @Override
    public int computeDefense() {
        return 0;
    }

    @Override
    public int computeAttack() {
        return 0;
    }

    /**
     * 召唤兽定时攻击调度
     */
    public void startPlayerAutoAttackScheduled() {
        //获取BOSS

        SkillService skillService = SpringContextUtil.getApplicationContext().getBean(SkillService.class);
        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            if(active){
                //随机选择召唤兽技能攻击
                Skill skill = this.getPlayerCallSkill().randomSkill();
                skillService.attack(this,skill);
            }
        }, 0L, 5L, TimeUnit.SECONDS);

    }

    private PlayerCallSkill getPlayerCallSkill() {
        return playerCallSkill;
    }

    /**
     * 启动召唤兽消失调度
     */
    public void startPlayerCallDisAppearScheduled(int continueTime) {
        //到达时间后，清理召唤兽事件
        scheduledExecutorService.schedule(() -> {
            EventBusHolder.register(new PlayerCallDisappearEvent(this));
        }, continueTime, TimeUnit.SECONDS);
    }

}
