package com.linlazy.mmorpglintingyang.module.scene.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.fight.defense.Defense;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import lombok.Data;

/**
 * 场景实体类
 */
@Data
public class SceneEntityDo {

    /**
     * 场景ID
     */
    private int sceneId;

    /**
     * 场景实体ID
     */
    private long sceneEntityId;

    /**
     * 场景实体类型
     */
    private int sceneEntityType;

    /**
     * 是否死亡
     */
    private boolean dead;

    /**
     * 实体名称
     */
    private String name;

    /**
     * 当前血量
     */
    private int hp;

    private GlobalConfigService globalConfigService = SpringContextUtil.getApplicationContext().getBean(GlobalConfigService.class);

    public SceneEntityDo(MonsterDo monsterDo) {
        this.sceneId = monsterDo.getSceneId();
        this.sceneEntityId = monsterDo.getMonsterId();
        this.name = monsterDo.getName();
        this.sceneEntityType = SceneEntityType.Monster;
        this.hp =monsterDo.getHp();
    }

    public SceneEntityDo(NpcDo npcDo) {
        this.sceneId = npcDo.getSceneId();
        this.sceneEntityId = npcDo.getNpcId();
        this.name = npcDo.getName();
        this.sceneEntityType = SceneEntityType.Npc;
        this.hp =npcDo.getHp();
    }
    public SceneEntityDo(ScenePlayerDo scenePlayerDo) {
        this.sceneId = scenePlayerDo.getSceneId();
        this.sceneEntityId = scenePlayerDo.getActorId();
        this.name = scenePlayerDo.getName();
        this.sceneEntityType = SceneEntityType.Player;
        this.hp =scenePlayerDo.getHp();
    }

    public SceneEntityDo(SceneBossDo sceneBossDo) {
        this.sceneId = sceneBossDo.getSceneId();
        this.sceneEntityId = sceneBossDo.getBossId();
        this.name = sceneBossDo.getName();
        this.sceneEntityType = SceneEntityType.Boss;
        this.hp =sceneBossDo.getHp();
    }


    public void attacked(int attack, JSONObject jsonObject){
        int entityType = jsonObject.getIntValue("entityType");
        long entityId = jsonObject.getLongValue("entityId");
        int defense = Defense.computeDefense(entityType, entityId, jsonObject);
        int damage = attack > defense?attack - defense: 1;

        this.hp -= damage;
        if(this.hp < 0){
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

        }

        //玩家受到伤害事件
        if(sceneEntityType == SceneEntityType.Player){
            jsonObject.put("damage",damage);
            triggerActorDamageEvent(jsonObject);
        }

    }


    /**
     * 玩家受到伤害事件
     * @param jsonObject
     */
    private void triggerActorDamageEvent(JSONObject jsonObject) {
        EventBusHolder.post(new ActorEvent<>(sceneEntityId,EventType.ACTOR_DAMAGE,jsonObject));
    }

    /**
     * 触发竞技场死亡事件
     * @param jsonObject
     */
    private void triggerArenaDeadEvent(JSONObject jsonObject) {
        //玩家死亡
        if(sceneEntityType == SceneEntityType.Player){
            EventBusHolder.post(new ActorEvent(sceneEntityId, EventType.ARENA_ACTOR_DEAD,jsonObject));
        }
    }

    /**
     * 触发副本死亡事件
     * @param jsonObject
     */
    private void triggerCopyDeadEvent(JSONObject jsonObject) {
        //玩家死亡
        if(sceneEntityType == SceneEntityType.Player){
            EventBusHolder.post(new ActorEvent(sceneEntityId, EventType.COPY_ACTOR_DEAD,jsonObject));
        }
        //BOSS死亡
        if(sceneEntityType == SceneEntityType.Boss){
            EventBusHolder.post(new ActorEvent(sceneEntityId, EventType.COPY_BOSS_DEAD,jsonObject));
        }
    }
}
