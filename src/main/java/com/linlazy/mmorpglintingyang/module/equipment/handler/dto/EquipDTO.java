package com.linlazy.mmorpglintingyang.module.equipment.handler.dto;

import com.linlazy.mmorpglintingyang.module.common.addition.Addition;
import com.linlazy.mmorpglintingyang.module.equipment.manager.domain.Equip;
import lombok.Data;

import java.util.List;

@Data
public class EquipDTO {

    /**
     * 所属玩家
     */
    private long actorId;
    /**
     * 装备ID
     */
    private long equipId;
    /**
     * 装备状态
     */
    private int status;

    /**
     * 加成（物理攻击，魔法攻击,物理防御，魔法防御）
     */
    List<Addition> additionList;

    /**
     * 耐久度
     */
    private int durability;

    public EquipDTO(Equip equip) {
        this.actorId = equip.getActorId();
        this.equipId = equip.getEquipId();
        this.durability = equip.getDurability();
        this.additionList = equip.getAdditionList();
    }

}
