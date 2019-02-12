package com.linlazy.mmorpg.module.scene.dto;

import com.linlazy.mmorpg.module.scene.constants.MonsterType;
import com.linlazy.mmorpg.module.scene.domain.Monster;
import lombok.Data;

/**
 * 怪物信息DTO
 * @author linlazy
 */
@Data
public class MonsterDTO {
    /**
     * 怪物标识
      */
    private Long id;
    /**
     * 怪物名称
     */
    private String monsterName;

    /**
     * 怪物类型
     */
    private Integer type;

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
        id = monster.getId();
        monsterName = monster.getName();
        hp = monster.getHp();
        attack = monster.computeAttack();
        defense = monster.computeDefense();
        this.type = monster.getType();

    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("小怪标识【%d】 小怪名称【%s】 攻击力【%d】 防御力【%d】 血量：%d 等级：【%d】",id,monsterName,attack,defense,hp,level));

        if(type == MonsterType.ACTIVE){
            stringBuilder.append("小怪类型【主动攻击】");
        }else {
            stringBuilder.append("小怪类型【被动攻击】");
        }

        return stringBuilder.toString();
    }
}
