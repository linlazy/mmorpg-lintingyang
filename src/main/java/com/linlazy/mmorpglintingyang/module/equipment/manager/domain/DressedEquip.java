package com.linlazy.mmorpglintingyang.module.equipment.manager.domain;

import com.linlazy.mmorpglintingyang.module.equipment.constants.EquipType;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.utils.RandomUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 已穿戴装备
 */
@Data
@Component
public class DressedEquip {

    @Autowired
    private ItemDao itemDao;

    /**
     *
     * @param attack 伤害
     */
    public void consumeDurabilityWithAttack(long actorId,int attack){
        itemDao.getItemSet(actorId).stream()
            .map(EquipDo::new)
            .filter(equipDo -> equipDo.isDressed())
            .filter(equipDo -> equipDo.getType() == EquipType.Arms)
            .findFirst()
            .ifPresent(
                equipDo ->  {
                EquipDurability.consumeDurability(equipDo,attack);
                itemDao.updateItem(equipDo.convertItem());
                }
            );
    }

    /**
     *
     * @param attacked 受到的伤害
     */
    public void consumeDurabilityWithAttacked(long actorId,int attacked){
        List<EquipDo> collect = itemDao.getItemSet(actorId).stream()
                .map(EquipDo::new)
                .filter(equipDo -> equipDo.isDressed())
                .filter(equipDo -> equipDo.getType() != EquipType.Arms)
                .collect(Collectors.toList());
        EquipDo equipDo = RandomUtils.randomElement(collect);
        EquipDurability.consumeDurability(equipDo,attacked);
        itemDao.updateItem(equipDo.convertItem());
    }


    /**
     * 计算伤害
     * @param actorId
     * @return
     */
    public int computeAttack(long actorId) {
        return itemDao.getItemSet(actorId).stream()
                .map(EquipDo::new)
                .filter(equipDo -> equipDo.isDressed())
                .map(equipDo -> equipDo.getPhysicalAttack())
                .reduce(0, (a, b) -> a + b);
    }
}
