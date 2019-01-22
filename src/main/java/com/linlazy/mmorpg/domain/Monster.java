package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.event.type.SceneMonsterDeadEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.push.PlayerPushHelper;
import com.linlazy.mmorpg.service.SceneService;
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
        int attack = sceneEntity.computeAttack();
        int defense = computeDefense();
        int damage = attack > defense?attack - defense: 1;
        this.hp -= damage;


        if(hp > 0){
            if(sceneEntity instanceof Player){
                Player player = (Player) sceneEntity;
                PlayerPushHelper.pushAttacked(player.getActorId(),String.format("%d 小怪【%s】受到玩家【%s】技能【%s】攻击：血量:%d", DateUtils.getNowMillis()/1000,name,sceneEntity.getName(),skill.getName(),hp));
            }
            if(sceneEntity instanceof PlayerCall){
                PlayerCall playerCall = (PlayerCall) sceneEntity;
                PlayerPushHelper.pushAttacked(playerCall.getSourceId(),String.format("%d 小怪【%s】受到玩家召唤兽【%s】技能【%s】攻击：血量:%d", DateUtils.getNowMillis()/1000,name,sceneEntity.getName(),skill.getName(),hp));
            }
        } else {
            this.hp = 0;
            triggerDeadEvent();
        }
    }


    @Override
    protected void triggerDeadEvent() {
        SceneService sceneService = SpringContextUtil.getApplicationContext().getBean(SceneService.class);
        Scene scene = sceneService.getSceneBySceneEntity(this);
        EventBusHolder.post(new SceneMonsterDeadEvent(scene,this));

    }
}
