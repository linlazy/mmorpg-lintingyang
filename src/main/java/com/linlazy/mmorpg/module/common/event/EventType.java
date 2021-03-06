package com.linlazy.mmorpg.module.common.event;

/**
 * @author linlazy
 */

public enum EventType {
    /**
     * 进入场景
     */
    SCENE_ENTER,
    /**
     * 玩家攻击造成伤害
     */
    ATTACK,
    /**
     * 登录
     */
    LOGIN,
    /**
     * 退出副本
     */
    QUIT_COPY,

    /**
     * 副本挑战成功
     */
    COPY_SUCCESS,
    /**
     * 副本挑战失败
     */
    COPY_FAIL,
    /**
     * 副本玩家死亡
     */
    COPY_ACTOR_DEAD,
    /**
     * 进入副本
     */
    ENTER_COPY_SCENE,
    /**
     * 副本BOSS死亡
     */
    COPY_BOSS_DEAD,

    /**
     * 竞技场玩家被杀死
     */
    ARENA_ACTOR_DEAD,
    /**
     * 场景实体死亡
     */
   SCENE_ENTITY_DEAD,
    /**
     * 玩家受到伤害
     */
    ACTOR_DAMAGE,
    /**
     * 场景怪物死亡
     */
    SCENE_MONSTER_DEAD,
    /**
     * 玩家等级提升事件
     */
    ACTOR_LEVEL_UP,
    /**
     * 玩家金币改变事件
     */
    ACTOR_GOLD_CHANGE,

    /**
     * 任务触发事件
     */
    TASK_TRIGGER,
    /**
     * 任务完成事件
     */
    TASK_COMPLETE,
    /**
     * 穿戴装备
     */
    DRESS_EQUIP,
    /**
     * 玩家死亡
     */
    ACTOR_DEAD
}
