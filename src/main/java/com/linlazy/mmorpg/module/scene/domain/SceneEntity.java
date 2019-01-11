package com.linlazy.mmorpg.module.scene.domain;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.fight.defense.Defense;
import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.scene.push.ScenePushHelper;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

/**
 * 场景实体类
 * @author linlazy
 */
@Data
public class SceneEntity {

    /**
     * 场景ID
     */
    protected int sceneId;

    /**
     * 实体名称
     */
    protected String name;

    /**
     * 当前血量
     */
    protected int hp;

    /**
     * 嘲讽状态
     */
    protected boolean tauntStatus;


    public void attacked(int attack, JSONObject jsonObject){

         GlobalConfigService globalConfigService = SpringContextUtil.getApplicationContext().getBean(GlobalConfigService.class);
        int entityType = jsonObject.getIntValue("entityType");
        long entityId = jsonObject.getLongValue("entityId");
        int defense = Defense.computeDefense(entityType, entityId, jsonObject);
        int damage = attack > defense?attack - defense: 1;

        this.hp -= damage;
        if(this.hp <= 0){
            this.hp = 0;
            EventBusHolder.post(new ActorEvent(0,EventType.SCENE_ENTITY_DEAD,jsonObject));

            //若为副本场景，触发副本场景相关的死亡事件
            if(globalConfigService.isCopy(sceneId)){
                triggerCopyDeadEvent(jsonObject);
            }

            //若为竞技场场景，触发竞技场场景相关的死亡事件
            if(globalConfigService.isArena(sceneId)){

                jsonObject.put("killId",jsonObject.getLongValue("actorId"));
                jsonObject.put("killedId",jsonObject.getLongValue("entityId"));
                triggerArenaDeadEvent(jsonObject);
            }

            //怪物死亡事件
            if(sceneEntityType == SceneEntityType.MONSTER){
                jsonObject.put("damage",damage);
                triggerMonsterDeadEvent(jsonObject);
            }
        }

        if(sceneEntityType == SceneEntityType.PLAYER){
            Player player = (Player)this;
            player.damageHp(damage);
        }

        long actorId = jsonObject.getLongValue("actorId");
        if( actorId != 0){
            EventBusHolder.post(new ActorEvent<>(jsonObject.getLongValue("actorId"),EventType.ATTACK));
            ScenePushHelper.pushSceneEntityDamage(jsonObject.getLongValue("actorId"), Lists.newArrayList(this));

        }
    }

    private void triggerMonsterDeadEvent(JSONObject jsonObject) {
        jsonObject.put("sceneId",sceneId);
        jsonObject.put("monsterDo",this);
        EventBusHolder.post(new ActorEvent(0,EventType.SCENE_MONSTER_DEAD,jsonObject));
    }


    /**
     * 触发竞技场死亡事件
     * @param jsonObject
     */
    private void triggerArenaDeadEvent(JSONObject jsonObject) {
        //玩家死亡
        if(sceneEntityType == SceneEntityType.PLAYER){
            EventBusHolder.post(new ActorEvent(sceneEntityId, EventType.ARENA_ACTOR_DEAD,jsonObject));
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
            EventBusHolder.post(new ActorEvent(sceneEntityId, EventType.COPY_ACTOR_DEAD,jsonObject));
        }
        //BOSS死亡
        if(sceneEntityType == SceneEntityType.BOSS){
            jsonObject.put("copyId",copyId);
            EventBusHolder.post(new ActorEvent(sceneEntityId, EventType.COPY_BOSS_DEAD,jsonObject));
        }
    }
}