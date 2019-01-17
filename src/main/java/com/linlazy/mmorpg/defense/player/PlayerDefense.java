package com.linlazy.mmorpg.defense.player;

import com.linlazy.mmorpg.domain.Player;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 玩家防御力
 * @author linlazy
 */
public abstract class PlayerDefense {


    private static Map<Integer, PlayerDefense> playerDefenseMap = new HashMap<>();

    @PostConstruct
    public void init(){
        playerDefenseMap.put(defenseType(),this);
    }

    /**
     * 防御力类型
     * @return
     */
    protected abstract int defenseType();

    /**
     * 计算玩家防御力
     * @return
     */
    protected abstract int computeDefense(Player player);

    public static int computeFinalDefense(Player player){
        return playerDefenseMap.values().stream()
                .map(playerAttack -> playerAttack.computeDefense(player))
                .reduce(0,(a,b) -> a + b);
    }
}
