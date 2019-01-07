package com.linlazy.mmorpglintingyang.module.domain;

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
    private long id;

    /**
     * boss技能
     */
    private Set<Skill> skillSet = new HashSet<>();
}
