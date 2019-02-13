package com.linlazy.mmorpg.module.scene.domain;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.event.type.SceneEnterEvent;
import com.linlazy.mmorpg.event.type.SceneMonsterDeadEvent;
import com.linlazy.mmorpg.event.type.SceneMoveEvent;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.module.player.constants.ProfessionType;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.push.PlayerPushHelper;
import com.linlazy.mmorpg.module.playercall.domain.PlayerCall;
import com.linlazy.mmorpg.module.scene.constants.MonsterType;
import com.linlazy.mmorpg.module.scene.service.SceneService;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 怪物
 * @author linlazy
 */
@Data
@Slf4j
public class Monster extends SceneEntity {

    private static final AtomicLong atomicLong = new AtomicLong(0);

    public Monster() {
        this.id =atomicLong.incrementAndGet();
        EventBusHolder.register(this);
    }


    private long id;
    private int sceneId;

    private long monsterId;

    /**
     * 怪物类型：主动攻击,被动攻击
     */
    private int type;

    private int attack;

    private Reward reward;

    /**
     * 小怪技能
     */
    private List<Skill> skillList = new ArrayList<>();


    private SceneEntity attackTarget;

    /**
     * 开始自动攻击句柄
     */
    private ScheduledFuture<?> startMonsterAutoAttackScheduled;

    /**
     * 取消自动攻击句柄
     */
    private ScheduledFuture<?> cancelAutoAttackSchedule;

    /**
     * 小怪调度线程池
     */
    private ScheduledExecutorService monsterScheduledExecutor = new ScheduledThreadPool(20);


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
     * @param sceneEnterEvent
     */
    @Subscribe
    public void moveToScene(SceneMoveEvent sceneEnterEvent){
        Player player = sceneEnterEvent.getPlayer();
        checkAttack(player);
    }



    private void checkAttack(Player player) {
        if(type == MonsterType.ACTIVE){

            if(startMonsterAutoAttackScheduled == null || startMonsterAutoAttackScheduled.isCancelled()){
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
        return attack;
    }

    @Override
    public Set<SceneEntity> getOtherAttackTarget(SceneEntity attackTarget, int attackNum) {
        Set<SceneEntity> result = Sets.newHashSet();

        Player player = null;
        if(attackTarget instanceof PlayerCall){
            PlayerCall playerCall = (PlayerCall) attackTarget;
            player = playerCall.player();
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


    public Skill randomSkill(){
        return RandomUtils.randomElement(skillList);
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }


    @Override
    public void attacked(SceneEntity sceneEntity, Skill skill) {
        super.attacked(sceneEntity,skill);

        if(hp > 0){
            if(sceneEntity instanceof Player){
                Player player = (Player) sceneEntity;
                PlayerPushHelper.pushAttacked(player.getActorId(),String.format("%d 小怪【%s】受到玩家【%s】技能【%s】攻击：血量:%d", DateUtils.getNowMillis()/1000,name,sceneEntity.getName(), skill.getName(),hp));
            }
            if(sceneEntity instanceof PlayerCall){
                PlayerCall playerCall = (PlayerCall) sceneEntity;
                PlayerPushHelper.pushAttacked(playerCall.getSourceId(),String.format("%d 小怪【%s】受到玩家召唤兽【%s】技能【%s】攻击：血量:%d", DateUtils.getNowMillis()/1000,name,sceneEntity.getName(), skill.getName(),hp));
            }

            if(skill.getType() == SkillType.TAUNT){
                attackTarget = sceneEntity;
            }

            if(startMonsterAutoAttackScheduled == null){
                attackTarget = sceneEntity;
                startAutoAttack();
            }
        } else {
            this.hp = 0;

            quitSchedule();

            if(sceneEntity instanceof Player){
                Player player = (Player) sceneEntity;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("monster",this);
                EventBusHolder.post(new ActorEvent(player.getActorId(), EventType.SCENE_MONSTER_DEAD,jsonObject));
            }


            triggerDeadEvent();
        }
    }

    public void quitSchedule() {

        if(cancelAutoAttackSchedule != null){
            cancelAutoAttackSchedule.cancel(true);
        }

        if(startMonsterAutoAttackScheduled != null){
            startMonsterAutoAttackScheduled.cancel(true);
        }
    }


    @Override
    protected void triggerDeadEvent() {
        super.triggerDeadEvent();



        SceneService sceneService = SpringContextUtil.getApplicationContext().getBean(SceneService.class);
        Scene scene = sceneService.getSceneBySceneEntity(this);
        EventBusHolder.post(new SceneMonsterDeadEvent(scene,this));

        if(sceneService.isCopyScene(scene.getSceneId())){

            RewardService rewardService = SpringContextUtil.getApplicationContext().getBean(RewardService.class);
            scene.getPlayerMap().values()
                    .stream()
                    .forEach(player -> {
                        rewardService.addRewardList(player.getActorId(), Lists.newArrayList(reward));
                        PlayerPushHelper.pushReward(player.getActorId(),"获得小怪奖励："+ reward.toString());
                    });
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
        startMonsterAutoAttackScheduled = monsterScheduledExecutor.scheduleAtFixedRate(() -> {
            //随机选择小怪技能攻击
            log.error("startMonsterAutoAttackScheduled");

            if(attackTarget.getSceneId() == sceneId && attackTarget.getHp() > 0){
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
        cancelAutoAttackSchedule = monsterScheduledExecutor.schedule(() -> {
            if (startMonsterAutoAttackScheduled != null) {
                startMonsterAutoAttackScheduled.cancel(true);
            }
        }, 10L, TimeUnit.SECONDS);
    }

    /**
     * 关闭取消自动攻击调度
     */
    private void closeOldCancelAutoAttack(){
        if (cancelAutoAttackSchedule != null) {
            log.error("closeOldCancelAutoAttack");
            cancelAutoAttackSchedule.cancel(true);
        }
    }

}
