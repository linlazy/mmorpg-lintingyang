package com.linlazy.mmorpg.module.player.attack;

import com.linlazy.mmorpg.module.equip.domain.Equip;
import com.linlazy.mmorpg.module.player.domain.Player;
import org.springframework.stereotype.Component;

/**
 * 玩家装备攻击力
 * @author linlazy
 */
@Component
public class PlayerEquipAttack extends PlayerAttack{



    @Override
    protected int attackType() {
        return 2;
    }

    @Override
    protected int computeAttack(Player player) {
       return   player.getDressedEquip().getEquipMap().values()
                .stream()
                .filter(equip -> equip.getDurability() > 0)
                .map(Equip::finalAttack)
                .reduce(0,(a ,b ) -> a + b);
    }
}
