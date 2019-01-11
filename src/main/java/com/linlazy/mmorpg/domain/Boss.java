package com.linlazy.mmorpg.domain;

import lombok.Data;

/**
 * BOSS
 * @author linlazy
 */
@Data
public class Boss {

    /**
     * boss标识
     */
    private long id;

    /**
     * 场景ID
     */
    private int sceneId;

    /**
     *  bossId
     */
    private long bossId;

    /**
     * boss血量
     */
    private int hp;

    /**
     * 攻击力
     */
    private int attack;

    /**
     * 名称
     */
    private String name;

}
