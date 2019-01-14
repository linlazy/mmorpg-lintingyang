package com.linlazy.mmorpg.constants;

/**
 * @author linlazy
 */
public interface TeamOperationType {

    /**
     * 邀请入队
     */
     int INVITE_JOIN = 1;

    /**
     * 接受入队
     */
    int ACCEPT_JOIN = 2;

    /**
     * 拒绝入队
     */
    int REJECT_JOIN = 3;
    /**
     * 离开队伍
     */
    int LEAVE = 4;
    /**
     * 踢出
     */
    int SHOT_OFF = 5;
}
