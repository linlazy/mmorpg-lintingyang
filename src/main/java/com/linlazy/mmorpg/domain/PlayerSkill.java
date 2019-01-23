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
                    stringBuilder.append(skill.toString()).append("\r\n");
                });

        return stringBuilder.toString();
    }
}
