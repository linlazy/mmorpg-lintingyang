package com.linlazy.mmorpg.domain;

import lombok.Data;

/**
 * 怪物
 * @author linlazy
 */
@Data
public class Monster extends SceneEntity {


    private int sceneId;

    private int monsterId;

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
