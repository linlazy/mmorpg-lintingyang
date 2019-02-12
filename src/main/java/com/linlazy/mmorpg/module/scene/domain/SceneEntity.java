package com.linlazy.mmorpg.module.scene.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import lombok.Data;

import java.util.Set;
import java.util.concurrent.ScheduledFuture;

/**
 * 场景实体类
 * @author linlazy
 */
@Data
public abstract class SceneEntity {


    /**
     * 场景实体Id
     */
    private long id;

    /**
     * 嘲讽状态调度
     */
    ScheduledFuture<?> tauntStatusScheduledFuture;
    /**
     * 场景ID
     */
    protected int sceneId;

    private long copyId;

    private int sceneEntityType;

    /**
     * 实体名称
     */
    protected String name;

    /**
     * 当前血量
     */
    protected int hp;

    private  int maxHP;

    /**
     * 蓝
     */
    protected int mp;

    private  int maxMP = 3000;



    /**
     * 嘲讽状态
     */
    protected boolean tauntStatus;


    /**
     * 计算防御
     * @return
     */
    public abstract int computeDefense();

    public void attacked(SceneEntity sceneEntity, Skill skill){
        JSONObject skillTemplateArgs = skill.getSkillTemplateArgs();
        int attack = sceneEntity.computeAttack() + skillTemplateArgs.getIntValue("attack");
        int defense = computeDefense();
        int damage = attack > defense?attack - defense: 1;
        this.hp -= damage;

    }

    protected  void triggerDeadEvent(){

        if(tauntStatusScheduledFuture != null){
            tauntStatusScheduledFuture.cancel(true);
        }
    }

    /**
     * 是否处于副本中
     * @return
     */
    public boolean isCopy(){
        return true;
    }



    /**
     * 触发竞技场死亡事件
     * @param jsonObject
     */
    private void triggerArenaDeadEvent(JSONObject jsonObject) {
        //玩家死亡
        if(sceneEntityType == SceneEntityType.PLAYER){
//            EventBusHolder.post(new ActorEvent(sceneEntityId, EventType.ARENA_ACTOR_DEAD,jsonObject));
        }
    }

    /**
     * 触发副本死亡事件
     * @param jsonObject
     */
    private void triggerCopyDeadEvent(JSONObject jsonObject) {
        //玩家死亡
        if(sceneEntityType == SceneEntityType.PLAYER){
            jsonObject.put("copyId",copyId);
//            EventBusHolder.post(new ActorEvent(sceneEntityId, EventType.COPY_ACTOR_DEAD,jsonObject));
        }
        //BOSS死亡
        if(sceneEntityType == SceneEntityType.BOSS){
            jsonObject.put("copyId",copyId);
//            EventBusHolder.post(new ActorEvent(sceneEntityId, EventType.COPY_BOSS_DEAD,jsonObject));
        }
    }

    public  int resumeMP(int resumeMP){
        synchronized (this){
            this.mp += resumeMP;
            if(this.mp >= maxMP){
                this.mp = maxMP;
            }
            return this.mp;
        }
    }



    public  int consumeMP(int mp){
        synchronized (this){
            this.mp -= mp;
            if(this.mp <0){
                this.mp = 0;
            }
            return this.mp;
        }
    }

    public  int resumeHP(int resumeHP){
        synchronized (this){
            this.hp += resumeHP;
            if(this.hp >= maxHP){
                this.hp = maxHP;
            }
            return this.hp;
        }
    }

    public  int consumeHP(int consumeHP){
        synchronized (this){
            this.hp -= consumeHP;
            if(this.hp >= maxHP){
                this.hp = maxHP;
            }
            return this.hp;
        }
    }

    /**
     * 计算攻击力
     * @return
     */
    public abstract int computeAttack();

    public  void active(){

    }

    /**
     * 更据攻击人数确定攻击对象集合
     * @param attackTarget
     * @param attackNum
     * @return
     */
    public abstract Set<SceneEntity> getOtherAttackTarget(SceneEntity attackTarget, int attackNum);
}
