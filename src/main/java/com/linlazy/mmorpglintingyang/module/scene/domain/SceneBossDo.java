package com.linlazy.mmorpglintingyang.module.scene.domain;

import lombok.Data;

@Data
public class SceneBossDo {

    /**
     * 场景ID
     */
    private int sceneId;

    /**
     * 副本ID
     */
    private int copyId;

    /**
     * bossID
     */
    private long bossId;

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
