package com.linlazy.mmorpg.module.player.defense;

import com.linlazy.mmorpg.module.equip.domain.Equip;
import com.linlazy.mmorpg.module.player.domain.Player;
import org.springframework.stereotype.Component;

/**
 * 玩家装备防御力
 * @author linlazy
 */
@Component
public class PlayerEquipDefense extends PlayerDefense{


    @Override
    protected int defenseType() {
        return 2;
    }

    @Override
    protected int computeDefense(Player player) {
        return   player.getDressedEquip().getEquipMap().values()
                .stream()
                .filter(equip -> equip.getDurability() > 0)
                .map(Equip::finalDefense)
                .reduce(0,(a ,b ) -> a + b);
    }
}
