package com.linlazy.mmorpglintingyang.module.fight.defense.actor;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public abstract class Defense {
    
    private static Map<Integer, Defense> map = new HashMap<>();
    
    @PostConstruct
    public void init(){
        map.put(defenseType(),this);
    }

    public abstract int defenseType();

    public static Defense getDefense(int defenseType){
        return map.get(defenseType);
    }
    
    public abstract int computeDefense(long actorId);
}
