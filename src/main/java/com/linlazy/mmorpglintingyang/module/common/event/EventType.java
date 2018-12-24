package com.linlazy.mmorpglintingyang.module.common.event;

public enum EventType {
    /**
     * 进入场景
     */
    SCENE_ENTER,
    /**
     * 玩家受到伤害
     */
    ATTACKED,
    /**
     * 玩家攻击伤害
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
    COPY_FAIL
}
