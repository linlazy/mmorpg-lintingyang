package com.linlazy.mmorpg.module.dto;

/**
 * @author linlazy
 */
public class BossDTO {

    /**
     * boss名称
     */
    private final String bossName;

    /**
     * boss血量
     */
    private final Integer hp;

    public BossDTO(String bossName, Integer hp) {
        this.bossName = bossName;
        this.hp = hp;
    }

    @Override
    public String toString() {
        return "BossDTO{" +
                "bossName='" + bossName + '\'' +
                ", hp=" + hp +
                '}';
    }
}
