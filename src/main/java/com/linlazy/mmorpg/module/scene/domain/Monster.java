package com.linlazy.mmorpg.module.scene.domain;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.event.type.SceneMonsterDeadEvent;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.push.PlayerPushHelper;
import com.linlazy.mmorpg.module.playercall.domain.PlayerCall;
import com.linlazy.mmorpg.module.scene.service.SceneService;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.RandomUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 怪物
 * @author linlazy
 */
@Data
public class Monster extends SceneEntity {

    private static final AtomicLong atomicLong = new AtomicLong(0);

    public Monster() {
        this.id =atomicLong.incrementAndGet();
    }


    private long id;
    private int sceneId;

    private int monsterId;

    private int attack;

    private Reward reward;

    /**
     * 小怪技能
     */
    private List<Skill> skillList = new ArrayList<>();



    @Override
    public int computeDefense() {
        return 0;
    }

    @Override
    public int computeAttack() {
        return attack;
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
        } else {
            this.hp = 0;
            if(sceneEntity instanceof Player){
                Player player = (Player) sceneEntity;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("monster",this);
                EventBusHolder.post(new ActorEvent(player.getActorId(), EventType.SCENE_MONSTER_DEAD,jsonObject));
            }
            triggerDeadEvent();
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
}
