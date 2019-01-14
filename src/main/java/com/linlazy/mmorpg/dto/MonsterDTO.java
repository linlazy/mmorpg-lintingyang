package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Monster;

/**
 * 怪物信息DTO
 * @author linlazy
 */
public class MonsterDTO {

    /**
     * 怪物名称
     */
    private String monsterName;

    /**
     * 怪物血量
     */
    private Integer hp;

    public MonsterDTO(Monster monster) {
    }

    @Override
    public String toString() {
        return "MonsterDTO{" +
                "monsterName='" + monsterName + '\'' +
                ", hp=" + hp +
                '}';
    }
}
