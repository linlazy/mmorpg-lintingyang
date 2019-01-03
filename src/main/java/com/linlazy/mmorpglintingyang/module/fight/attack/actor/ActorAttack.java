package com.linlazy.mmorpglintingyang.module.fight.attack.actor;

import com.alibaba.fastjson.JSONObject;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class ActorAttack {

    private static Map<Integer,ActorAttack> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(attackType(),this);
    }

    /**
     * 攻击类型
     * @return
     */
    public abstract int attackType();


    /**
     * 计算玩家攻击力
     * @param actorId
     * @param jsonObject
     * @return
     */
    public static int computeFinalAttack(long actorId, JSONObject jsonObject) {

        return map.values().stream()
                .filter(actorAttack -> actorAttack.isValid(actorId,jsonObject))
                .map(defense -> defense.computeAttack(actorId,jsonObject))
                .reduce(0,(a,b) -> a + b);
    }

    public abstract int computeAttack(long actorId,JSONObject jsonObject);

    /**
     * 默认生效
     * @param actorId 玩家ID
     * @param jsonObject 可变参数
     * @return 生效结果
     */
    public boolean isValid(long actorId,JSONObject jsonObject){
        return true;
    }
}
