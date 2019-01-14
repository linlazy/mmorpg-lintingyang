package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.utils.RandomUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Boss技能信息
 * @author linlazy
 */
public class BossSkillInfo {
    /**
     * 标识
     */
    private long bossId;

    /**
     * boss技能
     */
    private Set<Skill> skillSet = new HashSet<>();


    public Skill randomSkill(){
       return RandomUtils.randomElement(skillSet);
    }
}
