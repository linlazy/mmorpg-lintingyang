package com.linlazy.mmorpglintingyang.module.equip.dto;

import com.linlazy.mmorpglintingyang.module.equip.manager.domain.EquipDo;
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
    /**
     * 装备状态
     */
    private Integer status;


    /**
     * 耐久度
     */
    private Integer durability;

    public EquipDTO(EquipDo equipDo) {
        this.actorId = equipDo.getActorId();
        this.equipId = equipDo.getEquipId();
        this.durability = equipDo.getDurability();
    }

}
