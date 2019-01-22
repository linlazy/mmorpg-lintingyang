package com.linlazy.mmorpg.attack.playercall;

import com.linlazy.mmorpg.domain.PlayerCall;
import com.linlazy.mmorpg.domain.Skill;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 玩家召唤兽攻击
 * @author linlazy
 */
public abstract class PlayerCallAttack {
    private static Map<Integer, PlayerCallAttack> playerCallAttackMap = new HashMap<>();

    @PostConstruct
    public void init(){
        playerCallAttackMap.put(attackType(),this);
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
    protected abstract int computeAttack(PlayerCall playerCall,Skill skill);

    public static int computeFinalAttack(PlayerCall playerCall, Skill skill){
        return playerCallAttackMap.values().stream()
                .map(playerCallAttack -> playerCallAttack.computeAttack(playerCall,skill))
                .reduce(0,(a,b) -> a + b);
    }
}
