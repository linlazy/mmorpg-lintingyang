package com.linlazy.mmorpglintingyang.module.scene.domain;

import lombok.Data;

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
}
