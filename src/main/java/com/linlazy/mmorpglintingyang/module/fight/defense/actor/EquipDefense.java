package com.linlazy.mmorpglintingyang.module.fight.defense.actor;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.equip.manager.domain.EquipDo;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 装备防御力 = 基础防御力 + 装备等级 * 6
 */
@Component
public class EquipDefense extends Defense{

    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemConfigService itemConfigService;

    @Override
    public int defenseType() {
        return DefenseType.EQUIP;
    }

    @Override
    public int computeDefense(long actorId) {
        return itemDao.getItemSet(actorId).stream()
                .map(ItemDo::new)
                .map(EquipDo::new)
                .filter(equipDo -> equipDo.isDressed())
                .map(equipDo -> {
                    JSONObject itemConfig = itemConfigService.getItemConfig(equipDo.getBaseItemId());
                    int defense = itemConfig.getIntValue("defense");
                    return defense + equipDo.getLevel() * 6;
                }).reduce(0,(a,b) -> a + b);
    }
}
