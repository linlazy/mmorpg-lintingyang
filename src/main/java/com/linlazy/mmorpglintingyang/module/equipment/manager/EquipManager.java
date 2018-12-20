package com.linlazy.mmorpglintingyang.module.equipment.manager;

import com.linlazy.mmorpglintingyang.module.equipment.manager.domain.DressedEquip;
import com.linlazy.mmorpglintingyang.module.equipment.manager.domain.EquipDo;
import com.linlazy.mmorpglintingyang.module.equipment.manager.domain.EquipDurability;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipManager {

    @Autowired
    private ItemDao itemDao;

    /**
     * 卸下装备
     * @param equipDo 卸载装扮装备
     */
    public void unDressEquipment(long actorId, EquipDo equipDo) {
       new DressedEquip(actorId).unDressEquipment(equipDo);
    }

    /**
     * 装扮装备
     * @param equipDo 穿戴装备
     */
    public void dressEquipment(long actorId,EquipDo equipDo) {
        new DressedEquip(actorId).dressEquipment(equipDo);
    }

    /**
     * 完整修复装备耐久度
     * @param actorId 玩家ID
     * @param equipId 装备ID
     */
    public EquipDo fixEquipment(long actorId, long equipId) {
        Item item = itemDao.getItem(actorId, equipId);
        EquipDo equipDo = new EquipDo(item);
        EquipDurability.fixEquip(equipDo,equipDo.getDurabilityUp());
        itemDao.updateItem(equipDo.convertItem());
        return equipDo;
    }


    /**
     * 消耗装备耐久度
     */
    public EquipDo consumeDurability(long actorId, long equipId,int consumeDurability){

        Item item = itemDao.getItem(actorId, equipId);
        EquipDo equipDo = new EquipDo(item);
        equipDo.setEquipId(equipId);
        EquipDurability.consumeDurability(equipDo, consumeDurability);
        itemDao.updateItem( equipDo.convertItem());
        return equipDo;
    }

}
