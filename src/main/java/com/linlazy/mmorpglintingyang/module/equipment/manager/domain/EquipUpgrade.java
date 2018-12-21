package com.linlazy.mmorpglintingyang.module.equipment.manager.domain;

public class EquipUpgrade {

    public static EquipDo upgrade(EquipDo equipDo){
        int level = equipDo.getLevel();
        if(level < equipDo.getLevelUp()){
            equipDo.setLevel(level + 1);
        }
        return equipDo;
    }
}
