package com.linlazy.mmorpg.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家技能信息
 * @author linlazy
 */
@Data
public class PlayerSkill {

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 玩家技能
     */
    private Map<Integer,Skill> skillMap = new HashMap<>();

    public PlayerSkill(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        skillMap.values().stream()
                .forEach(skill -> {
                    stringBuilder.append(String.format("拥有技能【%s】 等级【%s】 ID【%d】",skill.getName(),skill.getLevel(),skill.getSkillId())).append("\r\n");
                });

        return stringBuilder.toString();
    }
}
