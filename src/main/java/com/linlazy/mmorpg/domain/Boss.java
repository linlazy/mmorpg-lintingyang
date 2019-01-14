package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import lombok.Data;

/**
 * BOSS
 * @author linlazy
 */
@Data
public class Boss extends SceneEntity {

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

    private BossSkillInfo bossSkillInfo;

    @Override
    protected int computeDefense() {
        return 0;
    }

    @Override
    public int computeAttack() {
        return 0;
    }
}
