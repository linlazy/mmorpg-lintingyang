package com.linlazy.mmorpg.module.equip.manager.domain;

import com.linlazy.mmorpg.domain.Equip;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 已穿戴装备
 * @author linlazy
 */
@Data
public class DressedEquip {


    private Map<Long, Equip> equipMap = new HashMap<>();

    /**
     *
     */
    public void consumeDurabilityWithAttack(long actorId){
//        itemDao.getItemSet(actorId).stream()
//            .map(ItemDo::new)
//            .map(EquipDo::new)
//            .filter(equipDo -> equipDo.isDressed())
//            .filter(equipDo -> equipDo.getType() == EquipType.ARMS)
//            .findFirst()
//            .ifPresent(
//                equipDo ->  {
//                EquipDurability.consumeDurability(equipDo,1);
//                itemDao.updateItem(equipDo.convertItemDo().convertItem());
//                }
//            );
    }

    /**
     *
     */
    public void consumeDurabilityWithAttacked(long actorId){
//        List<EquipDo> collect = itemDao.getItemSet(actorId).stream()
//                .map(ItemDo::new)
//                .map(EquipDo::new)
//                .filter(equipDo -> equipDo.isDressed())
//                .filter(equipDo -> equipDo.getType() != EquipType.ARMS)
//                .collect(Collectors.toList());
//        EquipDo equipDo = RandomUtils.randomElement(collect);
//        EquipDurability.consumeDurability(equipDo,1);
//        itemDao.updateItem(equipDo.convertItemDo().convertItem());
    }


}
