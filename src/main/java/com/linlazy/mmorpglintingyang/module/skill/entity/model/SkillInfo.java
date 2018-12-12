package com.linlazy.mmorpglintingyang.module.skill.entity.model;

import lombok.Data;

import java.util.Objects;

@Data
public class SkillInfo {

    /**
     * 技能ID
     */
    private int skillId;
    /**
     * 技能等级
     */
    private int level;

    public SkillInfo(int skillId, int level) {
        this.skillId = skillId;
        this.level = level;
    }

    public SkillInfo(int skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillInfo skillInfo = (SkillInfo) o;
        return skillId == skillInfo.skillId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillId);
    }
}
