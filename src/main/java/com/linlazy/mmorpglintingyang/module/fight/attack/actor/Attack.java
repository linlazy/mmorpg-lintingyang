package com.linlazy.mmorpglintingyang.module.fight.attack.actor;

import com.alibaba.fastjson.JSONObject;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public abstract class Attack {

    private static Map<Integer,Attack> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(attackType(),this);
    }

    public abstract int attackType();

    public static Attack getAttack(int attackType){
        return map.get(attackType);
    }

    public abstract int computeAttack(long actorId, JSONObject jsonObject);
}
