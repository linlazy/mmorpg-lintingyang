package com.linlazy.mmorpg.module.scene.domain;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class MonsterDo {

    /**
     * 场景ID
     */
    private int sceneId;

    /**
     * 怪物ID
     */
    private long monsterId;

    /**
     * 是否死亡
     */
    private boolean isDead;

    /**
     * 名称
     */
    private String name;

    /**
     * 当前血量
     */
    private int hp;
}
