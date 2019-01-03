package com.linlazy.mmorpglintingyang.module.equip.manager.domain;

import com.linlazy.mmorpglintingyang.module.equip.constants.EquipType;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.utils.RandomUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 已穿戴装备
 * @author linlazy
 */
@Data
@Component
public class DressedEquip {

    @Autowired
    private ItemDao itemDao;

    /**
     *
     */
    public void consumeDurabilityWithAttack(long actorId){
        itemDao.getItemSet(actorId).stream()
            .map(ItemDo::new)
            .map(EquipDo::new)
            .filter(equipDo -> equipDo.isDressed())
            .filter(equipDo -> equipDo.getType() == EquipType.ARMS)
            .findFirst()
            .ifPresent(
                equipDo ->  {
                EquipDurability.consumeDurability(equipDo,1);
                itemDao.updateItem(equipDo.convertItemDo().convertItem());
                }
            );
    }

    /**
     *
     */
    public void consumeDurabilityWithAttacked(long actorId){
        List<EquipDo> collect = itemDao.getItemSet(actorId).stream()
                .map(ItemDo::new)
                .map(EquipDo::new)
                .filter(equipDo -> equipDo.isDressed())
                .filter(equipDo -> equipDo.getType() != EquipType.ARMS)
                .collect(Collectors.toList());
        EquipDo equipDo = RandomUtils.randomElement(collect);
        EquipDurability.consumeDurability(equipDo,1);
        itemDao.updateItem(equipDo.convertItemDo().convertItem());
    }


}
