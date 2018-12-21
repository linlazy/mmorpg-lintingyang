package com.linlazy.mmorpglintingyang.module.equipment.handler.dto;

import com.linlazy.mmorpglintingyang.module.equipment.manager.domain.EquipDo;
import lombok.Data;

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
     * 耐久度
     */
    private int durability;

    public EquipDTO(EquipDo equipDo) {
        this.actorId = equipDo.getActorId();
        this.equipId = equipDo.getEquipId();
        this.durability = equipDo.getDurability();
    }

}
