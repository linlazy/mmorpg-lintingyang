package com.linlazy.mmorpglintingyang.module.item.service.additem;

import com.linlazy.mmorpglintingyang.module.equipment.handler.dto.EquipDTO;
import com.linlazy.mmorpglintingyang.module.equipment.manager.EquipManager;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public  class AddItemType2Equip extends AbstractAddItem {

    @Autowired
    private EquipManager equipManager;

    @Override
    protected int itemType() {
        return 2;
    }

    /**
     * 增加装备
     * @param actorId
     * @param baseItemId
     * @return
     */
    @Override
    public Result<?> addItem(long actorId, int baseItemId,int num) {
       List<EquipDTO> equipDTOS = new ArrayList<>();

       for(int i = 1; i <= num; i++){
           EquipDTO equipDTO = equipManager.addEquip(actorId, baseItemId);
           equipDTOS.add(equipDTO);
       }

        return Result.success(equipDTOS);
    }
}
