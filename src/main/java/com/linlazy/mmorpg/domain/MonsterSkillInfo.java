package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.utils.RandomUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 小怪技能信息
 * @author linlazy
 */
public class MonsterSkillInfo {


    /**
     *  小怪标识
     */
    private long monsterId;

    /**
     * boss技能
     */
    private Set<Skill> skillSet = new HashSet<>();

    public Skill randomSkill(){
        return RandomUtils.randomElement(skillSet);
    }
}
