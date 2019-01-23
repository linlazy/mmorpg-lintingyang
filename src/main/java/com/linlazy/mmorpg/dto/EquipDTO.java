package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Equip;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class EquipDTO {

    /**
     * 所属玩家
     */
    private Long actorId;
    /**
     * 装备ID
     */
    private Long equipId;

    private String name;


    private Integer level;

    private Integer attack;

    private Integer defense;


    /**
     * 耐久度
     */
    private Integer durability;

    public EquipDTO(Equip equip) {
        this.equipId = equip.getItemId();
        this.durability = equip.getDurability();
        level = equip.getLevel();
        name =equip.getName();
        attack = equip.finalAttack();
        defense = equip.finalDefense();
    }


    @Override
    public String toString() {

        return String.format(" 装备名称【%s】 攻击力【%d】 防御力【%d】 耐久度【%d】 等级【%d】 道具ID【%d】",name,attack,defense,durability,level,equipId);
    }
}
