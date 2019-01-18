package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Monster;
import lombok.Data;

/**
 * 怪物信息DTO
 * @author linlazy
 */
@Data
public class MonsterDTO {

    /**
     * 怪物名称
     */
    private String monsterName;

    /**
     * 怪物血量
     */
    private Integer hp;

    /**
     * 攻击力
     */
    private Integer attack;

    /**
     * 防御力
     */
    private Integer defense;

    /**
     * 等级
     */
    private Integer level;

    public MonsterDTO(Monster monster) {
        monsterName = monster.getName();
        hp = monster.getHp();
        attack = monster.computeAttack();
        defense = monster.computeDefense();
    }

    @Override
    public String toString() {
        return String.format("小怪名称【%s】 攻击力【%d】 防御力【%d】 血量：%d 等级：【%d】",monsterName,attack,defense,hp,level);
    }
}
