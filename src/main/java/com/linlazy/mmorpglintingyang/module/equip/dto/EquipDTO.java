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
