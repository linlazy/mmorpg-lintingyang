package com.linlazy.mmorpglintingyang.module.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 玩家技能信息
 * @author linlazy
 */
public class PlayerSkillInfo {

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 玩家技能
     */
    private Set<Skill> skillSet = new HashSet<>();
}
