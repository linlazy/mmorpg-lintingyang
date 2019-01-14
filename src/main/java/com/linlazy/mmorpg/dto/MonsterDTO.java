package com.linlazy.mmorpg.dto;

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

    @Override
    public String toString() {
        return "MonsterDTO{" +
                "monsterName='" + monsterName + '\'' +
                ", hp=" + hp +
                '}';
    }
}
