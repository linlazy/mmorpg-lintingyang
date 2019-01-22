package com.linlazy.mmorpg.domain;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.event.type.PlayerAttackEvent;
import com.linlazy.mmorpg.event.type.PlayerAttackedEvent;
import com.linlazy.mmorpg.event.type.PlayerCallDisappearEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.push.PlayerCallPushHelper;
import com.linlazy.mmorpg.push.PlayerPushHelper;
import com.linlazy.mmorpg.service.SkillService;
import com.linlazy.mmorpg.utils.RandomUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private long id;

    private long sourceId;

    private int level;

    private List<Skill> skillList = new ArrayList<>();

    Skill randomSkill(){
        return RandomUtils.randomElement(skillList);
    }

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> autoAttackScheduledFuture;



    public PlayerCall(Player player) {
        sourceId = player.getActorId();

        EventBusHolder.register(this);
    }

    @Subscribe
    public void playerAttackEvent(PlayerAttackEvent playerAttackEvent){
        if(playerAttackEvent.getPlayer().getActorId() == sourceId){
            startPlayerAutoAttackScheduled();
        }
    }


    @Subscribe
    public void playerAttackedEvent(PlayerAttackedEvent playerAttackedEvent){
        if(playerAttackedEvent.getPlayer().getActorId() == sourceId){
            startPlayerAutoAttackScheduled();
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

        SkillService skillService = SpringContextUtil.getApplicationContext().getBean(SkillService.class);
        autoAttackScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
//            if(active){
                //随机选择召唤兽技能攻击
                Skill skill = this.randomSkill();
            PlayerPushHelper.pushAttack(sourceId,String.format("您的召唤兽【%s】使用了【%s】技能",this.name,skill.getName()));
            skillService.useSkill(this,skill);
//            }
        }, 0L, 5L, TimeUnit.SECONDS);

    }


    /**
     * 启动召唤兽消失调度
     */
    public void startPlayerCallDisAppearScheduled(int continueTime) {
        //到达时间后，清理召唤兽事件
        scheduledExecutorService.schedule(() -> {
            PlayerCallPushHelper.pushDisappear(this.getSourceId(),String.format("你的召唤兽【%s】到达时间后消失",this.getName()));
            EventBusHolder.post(new PlayerCallDisappearEvent(this));
            logger.debug("到达时间后，触发召唤兽消失事件");
        }, continueTime, TimeUnit.SECONDS);
    }

    public void quitAutoAttack() {
        if(autoAttackScheduledFuture != null){
            autoAttackScheduledFuture.cancel(true);
        }
    }


    @Override
    public void attacked(SceneEntity sceneEntity, Skill skill) {
        super.attacked(sceneEntity, skill);

        if(this.hp <= 0){
            this.hp = 0;
            triggerDeadEvent();
        }
    }

    @Override
    protected void triggerDeadEvent() {
        PlayerCallPushHelper.pushDisappear(this.getSourceId(),String.format("你的召唤兽【%s】已死亡",this.getName()));
        EventBusHolder.post(new PlayerCallDisappearEvent(this));
    }
}
