package com.linlazy.mmorpglintingyang.module.equipment.manager.domain;

import com.linlazy.mmorpglintingyang.module.equipment.constants.EquipType;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.utils.RandomUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 已穿戴装备
 */
@Data
@Component
public class DressedEquip {

    /**
     * 已穿戴装备集合
     */
    private Set<EquipDo> equipDoSet;

    @Autowired
    private ItemDao itemDao;

    public DressedEquip() {
    }

    public DressedEquip(long actorId) {
         this.equipDoSet = itemDao.getItemSet(actorId).stream()
                .map(EquipDo::new)
                .filter(equipDo -> equipDo.isDressed())
                .collect(Collectors.toSet());
    }


    /**
     *
     * @param attack 伤害
     */
    public void consumeDurabilityWithAttack(int attack){
        equipDoSet.stream()
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
    public void consumeDurabilityWithAttacked(int attacked){
        List<EquipDo> collect = equipDoSet.stream()
                .filter(equipDo -> equipDo.getType() != EquipType.Arms)
                .collect(Collectors.toList());
        EquipDo equipDo = RandomUtils.randomElement(collect);
        EquipDurability.consumeDurability(equipDo,attacked);
        itemDao.updateItem(equipDo.convertItem());
    }

    /**
     * 卸载装备
     */
    public void unDressEquipment(EquipDo equipDo){
        equipDoSet.remove(equipDo);
    }


    /**
     * 穿戴装备
     * @param dressEquipDo
     */
    public void dressEquipment(EquipDo dressEquipDo){
        //是否穿戴相同类型装备
        equipDoSet.stream()
                .filter(equipDo -> equipDo.getType() == dressEquipDo.getType())
                .findFirst()
                .ifPresent(
                        equipDo -> equipDoSet.remove(equipDo)
                );

        equipDoSet.add(dressEquipDo);
    }

}
