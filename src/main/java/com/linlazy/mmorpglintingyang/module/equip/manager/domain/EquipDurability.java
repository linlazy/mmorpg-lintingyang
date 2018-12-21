package com.linlazy.mmorpglintingyang.module.equip.manager.domain;

/**
 * 装备耐久度领域类
 */
public class EquipDurability {

    /**
     * 修理装备耐久度
     */
    public static EquipDo fixEquip(EquipDo equipDo, int addDurability){

        if(equipDo.getDurability() + addDurability< equipDo.getDurabilityUp()){
            equipDo.setDurability(equipDo.getDurabilityUp());
        }else {
            equipDo.setDurability(equipDo.getDurabilityUp());
        }
        return equipDo;
    }


    /**
     * 消耗装备耐久度
     */
    public static EquipDo consumeDurability(EquipDo equipDo,int consumeDurability){

        if(equipDo.getDurability() < consumeDurability){
            equipDo.setDurability(0);
        }else {
            equipDo.setDurability(equipDo.getDurability() - consumeDurability);
        }
        return equipDo;
    }
}