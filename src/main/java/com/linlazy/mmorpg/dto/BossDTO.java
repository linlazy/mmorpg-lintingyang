package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Boss;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
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
}
