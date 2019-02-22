package com.linlazy.mmorpg.module.scene.domain;

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.event.type.BossDeadEvent;
import com.linlazy.mmorpg.event.type.CopyBossDeadEvent;
import com.linlazy.mmorpg.event.type.SceneEnterEvent;
import com.linlazy.mmorpg.event.type.SceneMoveEvent;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.copy.fighting.domain.FightingCopy;
import com.linlazy.mmorpg.module.copy.fighting.event.CopyFightingBossDeadEvent;
import com.linlazy.mmorpg.module.player.constants.ProfessionType;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.push.PlayerPushHelper;
import com.linlazy.mmorpg.module.playercall.domain.PlayerCall;
import com.linlazy.mmorpg.module.scene.constants.MonsterType;
import com.linlazy.mmorpg.module.scene.copy.domain.Copy;
import com.linlazy.mmorpg.module.scene.copy.service.CopyService;
import com.linlazy.mmorpg.module.skill.constants.SkillType;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.module.skill.service.SkillService;
import com.linlazy.mmorpg.server.threadpool.ScheduledThreadPool;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.RandomUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * BOSS
 * @author linlazy
 */
@Data
@Slf4j
public class Boss extends SceneEntity {

    /**
     * BOSS调度线程池
     */
    ScheduledExecutorService bossScheduledExecutor = new ScheduledThreadPool(20);

    /**
     * boss标识
     */
    private long id;

    /**
     * boss类型：主动攻击，被动攻击
     */
    private int type;
    /**
     * 场景ID
     */
    private int sceneId;

    /**
     *  bossId
     */
    private long bossId;


    private int attack;
    /**
     * 名称
     */
    private String name;

    private Reward reward;

    /**
     * 状态
     */
    private int status;

    /**
     * 攻击目标
     */
    private SceneEntity attackTarget;

    /**
     * 开始自动攻击句柄
     */
    ScheduledFuture<?> startBossAutoAttackScheduled;

    /**
     * 取消自动攻击句柄
     */
    ScheduledFuture<?> cancelAutoAttackSchedule;

    /**
     * boss技能列表
     */
    private List<Skill> skillList = new ArrayList<>();

    public Skill randomSkill(){
        return RandomUtils.randomElement(skillList);
    }



    /**
     *
     *
     * 受到攻击，发起攻击
     * 若boss死亡，发送通知
     * 若boss嘲讽生效，boss改变攻击对象
     * 若boss处于正常状态，boss进入战斗状态，开始攻击发起攻击者
     * 若攻击者为玩家，通知玩家攻击效果，boss血量等变化
     * 若攻击者为玩家辅助对象（召唤兽等），通知玩家攻击效果，boss血量等变化
     *


    /**
     * 玩家进入场景，主动类型的怪物出于正常状态的发起攻击
     * @param sceneEnterEvent
     */
    @Subscribe
    public void enterScene(SceneEnterEvent sceneEnterEvent){
        Player player = sceneEnterEvent.getPlayer();
        checkAttack(player);

    }

    /**
     * 玩家进入场景，主动类型的怪物出于正常状态的发起攻击
     * @param sceneMoveEvent
     */
    @Subscribe
    public void moveToScene(SceneMoveEvent sceneMoveEvent){
        Player player = sceneMoveEvent.getPlayer();
        checkAttack(player);

    }

    public void checkAttack(Player player) {

        if(type == MonsterType.ACTIVE){

            if(startBossAutoAttackScheduled == null  || startBossAutoAttackScheduled.isCancelled()){
                attackTarget = player;
                if (player.getProfession() == ProfessionType.summoner){
                    PlayerCall playerCall = player.getPlayerCall();
                    if(playerCall != null){
                        attackTarget = playerCall;
                    }
                }
                startAutoAttack();
            }
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

    @Override
    public Set<SceneEntity> getOtherAttackTarget(SceneEntity attackTarget, int attackNum) {
        Set<SceneEntity> result =Sets.newHashSet();



        Player player = null;
        if(attackTarget instanceof PlayerCall){
            PlayerCall playerCall = (PlayerCall) attackTarget;
            player = playerCall.getSourcePlayer();
            result.add(player);
        }else {
            player = (Player) attackTarget;
        }

        if(player.isTeam()){
            Player finalPlayer = player;
            Set<Player> collect = player.getTeam().getPlayerTeamInfoMap().values().stream()
                    .map(playerTeamInfo -> playerTeamInfo.getPlayer())
                    .filter(player1 -> player1.getActorId() != finalPlayer.getActorId())
                    .collect(Collectors.toSet());

            result.addAll(collect);
        }



        return result.stream()
                .limit(attackNum).collect(Collectors.toSet());
    }


    @Override
    public void attacked(SceneEntity sceneEntity, Skill skill) {
        super.attacked(sceneEntity,skill);

        if(hp > 0){
            if(sceneEntity instanceof Player){
                Player player = (Player) sceneEntity;
                PlayerPushHelper.pushAttacked(player.getActorId(),String.format("%d BOSS【%s】受到玩家【%s】技能【%s】攻击：血量:%d", DateUtils.getNowMillis()/1000,name,sceneEntity.getName(), skill.getName(),hp));
            }
            if(sceneEntity instanceof PlayerCall){
                PlayerCall playerCall = (PlayerCall) sceneEntity;
                PlayerPushHelper.pushAttacked(playerCall.getSourcePlayer().getActorId(),String.format("%d BOSS【%s】受到玩家召唤兽【%s】技能【%s】攻击：血量:%d", DateUtils.getNowMillis()/1000,name,sceneEntity.getName(), skill.getName(),hp));
            }

            if(skill.getType() == SkillType.TAUNT){
                attackTarget = sceneEntity;
            }

            if(startBossAutoAttackScheduled == null || startBossAutoAttackScheduled.isCancelled()){
                attackTarget = sceneEntity;
                startAutoAttack();
            }
        } else {

            if(sceneEntity instanceof Player){
                Player player = (Player) sceneEntity;
                player.addExp(200);
                PlayerPushHelper.pushMessage(player.getActorId(),String.format("%d BOSS【%s】死亡", DateUtils.getNowMillis()/1000,name));
            }
            if(sceneEntity instanceof PlayerCall){
                PlayerCall playerCall = (PlayerCall) sceneEntity;
                Player sourcePlayer = playerCall.getSourcePlayer();
                sourcePlayer.addExp(200);
                PlayerPushHelper.pushMessage(sourcePlayer.getActorId(),String.format("%d BOSS【%s】死亡", DateUtils.getNowMillis()/1000,name));
            }

            this.hp = 0;
            quitSchedule();
            triggerDeadEvent();
        }
    }

    public void quitSchedule() {
        if(startBossAutoAttackScheduled != null && !startBossAutoAttackScheduled.isCancelled()){
            startBossAutoAttackScheduled.cancel(true);
        }
        if(cancelAutoAttackSchedule != null && !cancelAutoAttackSchedule.isCancelled()){
            cancelAutoAttackSchedule.cancel(true);
        }
    }

    /**
     * 自动攻击
     * 开启自动攻击，每隔2秒攻击目标
     * 若攻击目标不在同场景，则不攻击
     * 若不攻击超过10秒，攻击停止
     */
    public void startAutoAttack() {

        SkillService skillService = SpringContextUtil.getApplicationContext().getBean(SkillService.class);
        startBossAutoAttackScheduled = bossScheduledExecutor.scheduleAtFixedRate(() -> {
            //随机选择boss技能攻击
            log.error("startBossAutoAttackScheduled");

            if(attackTarget.getSceneId() == sceneId &&attackTarget.getHp() > 0){
                Skill skill = randomSkill();
                skillService.attack(this,skill,attackTarget);
                closeOldCancelAutoAttack();
                startNewCancelAutoAttack();
            }
        }, 0L, 2L, TimeUnit.SECONDS);
    }


    /**
     * 开启取消自动攻击调度
     */
    private void startNewCancelAutoAttack(){
        log.error("startNewCancelAutoAttack");
        cancelAutoAttackSchedule = bossScheduledExecutor.schedule(() -> {
            if (startBossAutoAttackScheduled != null && !startBossAutoAttackScheduled.isCancelled()) {
                startBossAutoAttackScheduled.cancel(true);
            }
        }, 10L, TimeUnit.SECONDS);
    }

    /**
     * 关闭取消自动攻击调度
     */
    private void closeOldCancelAutoAttack(){
        if (cancelAutoAttackSchedule != null && !cancelAutoAttackSchedule.isCancelled()) {
            log.error("closeOldCancelAutoAttack");
            cancelAutoAttackSchedule.cancel(true);
        }
    }

    @Override
    protected void triggerDeadEvent() {
        super.triggerDeadEvent();

        EventBusHolder.post(new BossDeadEvent(this));
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        if(sceneConfigService.isCopyScene(this.sceneId)){
            CopyService copyService = SpringContextUtil.getApplicationContext().getBean(CopyService.class);
            Copy copy = copyService.getCopy(this.getCopyId());
            copy.getPlayerMap().values().stream()
                    .forEach(player ->PlayerPushHelper.pushAttacked(player.getActorId(),String.format("%d BOSS【%s】已死亡", DateUtils.getNowMillis()/1000,name)));
            EventBusHolder.post(new CopyBossDeadEvent(copy));


            if(sceneConfigService.isFightCopyScene(this.sceneId)){
                EventBusHolder.post(new CopyFightingBossDeadEvent((FightingCopy) copy));
            }
        }
    }
}
