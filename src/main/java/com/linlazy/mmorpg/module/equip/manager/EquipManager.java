package com.linlazy.mmorpg.module.equip.manager;

import com.linlazy.mmorpg.dao.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class EquipManager {

    @Autowired
    private ItemDAO itemDAO;

    /**
     * 卸下装备
     * @param equipId 卸载装扮装备
     */
    public void unDressEquipment(long actorId, long equipId) {
//        Item item = itemDAO.getEntityByPK(actorId, equipId);
//        EquipDo equipDo = new EquipDo(new ItemDo(item));
//        equipDo.setDressed(false);
//        itemDao.updateItem(equipDo.convertItemDo().convertItem());
    }

    /**
     * 装扮装备
     * @param equipId 穿戴装备
     */
    public void dressEquipment(long actorId,long equipId) {
//        Item item = itemDao.getItem(actorId, equipId);
//        EquipDo equipDo = new EquipDo(new ItemDo(item));
//
//        //已穿戴装备
//        Set<EquipDo> dressedEquipDoSet = itemDao.getItemSet(actorId).stream()
//                .map(ItemDo::new)
//                .map(EquipDo::new)
//                .filter(equipDo1 -> equipDo1.isDressed())
//                .collect(Collectors.toSet());
//
//        //更新相同装备类型更新为未穿戴
//        for(EquipDo dressEquipDo : dressedEquipDoSet){
//            if(dressEquipDo.getType() == equipDo.getType()){
//                dressEquipDo.setDressed(false);
//                itemDao.updateItem(dressEquipDo.convertItemDo().convertItem());
//            }
//        }
//
//        //更新装备为已穿戴
//        equipDo.setDressed(true);
//        itemDao.updateItem(equipDo.convertItemDo().convertItem());
    }

    /**
     * 完整修复装备耐久度
     * @param actorId 玩家ID
     * @param equipId 装备ID
     */
//    public EquipDo fixEquipment(long actorId, long equipId) {
////        Item item = itemDao.getItem(actorId, equipId);
////        EquipDo equipDo = new EquipDo(new ItemDo(item));
////        EquipDurability.fixEquip(equipDo,equipDo.getDurabilityUp());
////        itemDao.updateItem(equipDo.convertItemDo().convertItem());
////        return equipDo;
//    }


//    /**
//     * 消耗装备耐久度
//     */
//    public EquipDo consumeDurability(long actorId, long equipId,int consumeDurability){
////
////        Item item = itemDao.getItem(actorId, equipId);
////        EquipDo equipDo = new EquipDo(new ItemDo(item));
////        EquipDurability.consumeDurability(equipDo, consumeDurability);
////        itemDao.updateItem( equipDo.convertItemDo().convertItem());
////        return equipDo;
//    }

    /**
     * 强化装备
     * @param actorId
     * @param equipId
     * @return
     */
//    public  EquipDo upgradeEquip(long actorId ,long equipId){
////        Item item = itemDao.getItem(actorId, equipId);
////        EquipDo equipDo = new EquipDo(new ItemDo(item));
////        EquipDo upgrade = EquipUpgrade.upgrade(equipDo);
////        return upgrade;
//    }
}
