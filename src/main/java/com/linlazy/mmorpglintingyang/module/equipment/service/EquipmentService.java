package com.linlazy.mmorpglintingyang.module.equipment.service;

import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.equipment.constants.EquipStatus;
import com.linlazy.mmorpglintingyang.module.equipment.manager.EquipManager;
import com.linlazy.mmorpglintingyang.module.equipment.manager.entity.Equip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipmentService {

    @Autowired
    private EquipManager equipManager;

    /**
     * 装备
     * @param actorId
     * @param equipId
     * @return
     */
    public Result<?> equip(long actorId, long equipId){
        //校验
        Equip equipment = equipManager.getEquipment(actorId, equipId);
        if(equipment == null || equipment.getStatus() == EquipStatus.Equiped){
            return Result.valueOf("参数有误");
        }

        // 1 查找原穿着装备
        Equip equip = equipManager.findOldDressedEquip(actorId,equipId);
        // 2 卸下装备 ，更新旧装备状态 去掉装备加成
        if(equip != null){
            equipManager.unDressEquipment(actorId,equip);
        }
        // 3 穿着装备 ，装备加成 更新新装备状态
        equipManager.dressEquipment(actorId,equipment);
        return Result.success();
    }


    /**
     * 卸载装备
     * @param actorId
     * @param equipId
     * @return
     */
    public Result<?> unEquip(long actorId, long equipId) {
        Equip equipment = equipManager.getEquipment(actorId, equipId);
        if(equipment == null || equipment.getStatus() == EquipStatus.UnEquiped){
            return Result.valueOf("参数有误");
        }
        equipManager.unDressEquipment(actorId,equipment);
        return Result.success();
    }
}
