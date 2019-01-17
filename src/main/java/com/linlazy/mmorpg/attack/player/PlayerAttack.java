package com.linlazy.mmorpg.attack.player;

import com.linlazy.mmorpg.domain.Player;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class PlayerAttack {

    private static Map<Integer,PlayerAttack> playerAttackMap = new HashMap<>();

    @PostConstruct
    public void init(){
        playerAttackMap.put(attackType(),this);
    }

    /**
     * 攻击力类型
     * @return
     */
    protected abstract int attackType();

    /**
     * 计算玩家攻击力
     * @return
     */
    protected abstract int computeAttack(Player player);

    public static int computeFinalAttack(Player player){
        return playerAttackMap.values().stream()
                .map(playerAttack -> playerAttack.computeAttack(player))
                .reduce(0,(a,b) -> a + b);
    }

}
