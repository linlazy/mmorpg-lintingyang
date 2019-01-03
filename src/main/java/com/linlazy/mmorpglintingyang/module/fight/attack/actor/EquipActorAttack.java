package com.linlazy.mmorpglintingyang.module.fight.attack.actor;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.equip.manager.domain.EquipDo;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 装备攻击力 = 基础攻击力 + 装备等级 * 6
 * @author linlazy
 */
@Component
public class EquipActorAttack extends ActorAttack {

    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemConfigService itemConfigService;


    @Override
    public int attackType() {
        return AttackType.EQUIP;
    }

    @Override
    public boolean isValid(long actorId,JSONObject jsonObject){
        return true;
    }

    @Override
    public int computeAttack(long actorId, JSONObject jsonObject) {

        return itemDao.getItemSet(actorId).stream()
                .map(ItemDo::new)
                .map(EquipDo::new)
                .filter(equipDo -> equipDo.isDressed()&&equipDo.getDurability()>0)
                .map(equipDo -> {
                    JSONObject itemConfig = itemConfigService.getItemConfig(equipDo.getBaseItemId());
                    int attack = itemConfig.getIntValue("attack");
                    return attack + equipDo.getLevel() * 6;
                }).reduce(0,(a,b) -> a + b);
    }

}
