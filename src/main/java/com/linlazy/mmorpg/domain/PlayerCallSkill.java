package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.utils.RandomUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author linlazy
 */
public class PlayerCallSkill {

    private long id;

    private Set<Skill> skillSet = new HashSet<>();

    Skill randomSkill(){
        return RandomUtils.randomElement(skillSet);
    }
}
