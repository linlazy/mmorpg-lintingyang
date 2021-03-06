package com.linlazy.mmorpg.module.scene.dto;

import com.linlazy.mmorpg.module.scene.domain.Boss;
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

    private Integer attack;

    private Integer defense;

    private Integer level;

    public BossDTO(Boss boss) {
       this.bossName = boss.getName();
       this.hp = boss.getHp();
       this.attack = boss.computeAttack();
       this.defense = boss.computeDefense();
    }

    @Override
    public String toString() {
        return String.format("BOSS名称【%s】 攻击力【%d】 防御力【%d】 血量：%d 等级：【%d】",bossName,attack,defense,hp,level);
    }
}
