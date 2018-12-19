package com.linlazy.mmorpglintingyang.module.equip;

import com.linlazy.mmorpglintingyang.module.backpack.ItemDo;
import lombok.Data;

import java.util.Objects;

@Data
public class EquipDo extends ItemDo {

    /**
     * 物理攻击下限
     */
    int physicalAttackDown;
    /**
     * 物理攻击上限
     */
    int physicalAttackUp;

    /**
     * 魔法攻击下限
     */
    int magicAttackDown;

    /**
     * 魔法攻击上限
     */
    int magicAttackUp;

    /**
     * 物理防御下限
     */
    int physicalDefenseDown;
    /**
     * 物理防御上限
     */
    int physicalDefenseUp;

    /**
     * 魔法防御下限
     */
    int magicDefenseDown;

    /**
     * 魔法防御上限
     */
    int magicDefenseUp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EquipDo equipDo = (EquipDo) o;
        return physicalAttackDown == equipDo.physicalAttackDown &&
                physicalAttackUp == equipDo.physicalAttackUp &&
                magicAttackDown == equipDo.magicAttackDown &&
                magicAttackUp == equipDo.magicAttackUp &&
                physicalDefenseDown == equipDo.physicalDefenseDown &&
                physicalDefenseUp == equipDo.physicalDefenseUp &&
                magicDefenseDown == equipDo.magicDefenseDown &&
                magicDefenseUp == equipDo.magicDefenseUp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), physicalAttackDown, physicalAttackUp, magicAttackDown, magicAttackUp, physicalDefenseDown, physicalDefenseUp, magicDefenseDown, magicDefenseUp);
    }
}
