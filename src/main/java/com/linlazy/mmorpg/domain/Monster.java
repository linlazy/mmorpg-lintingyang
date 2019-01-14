package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import lombok.Data;

/**
 * 怪物
 * @author linlazy
 */
@Data
public class Monster extends SceneEntity {

    private int sceneId;


    /**
     * 小怪技能信息
     */
    private MonsterSkillInfo monsterSkillInfo;

    @Override
    protected int computeDefense() {
        return 0;
    }

    @Override
    public int computeAttack() {
        return 0;
    }


    public MonsterSkillInfo getMonsterSkillInfo(){
        return monsterSkillInfo;
    }


}
