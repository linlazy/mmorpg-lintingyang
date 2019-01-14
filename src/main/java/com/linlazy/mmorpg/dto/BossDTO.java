package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Boss;

/**
 * @author linlazy
 */
public class BossDTO {

    /**
     * boss名称
     */
    private  String bossName;

    /**
     * boss血量
     */
    private  Integer hp;

    public BossDTO(Boss boss) {
       this.bossName = boss.getName();
       this.hp = boss.getHp();

    }

    public BossDTO(String bossName, Integer hp) {
    }

    @Override
    public String toString() {
        return "BossDTO{" +
                "bossName='" + bossName + '\'' +
                ", hp=" + hp +
                '}';
    }
}
