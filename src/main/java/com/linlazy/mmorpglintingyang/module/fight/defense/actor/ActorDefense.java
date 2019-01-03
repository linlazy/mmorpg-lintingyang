package com.linlazy.mmorpglintingyang.module.fight.defense.actor;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.defense.Defense;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class ActorDefense extends Defense {

    private static Map<Integer, ActorDefense> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(actorDefenseType(),this);
    }

    public abstract int actorDefenseType();

    public static int computeFinalDefense(long actorId, JSONObject jsonObject) {
        return map.values().stream()
                .filter(actorDefense -> actorDefense.isValid(actorId,jsonObject))
                .map(defense -> defense.computeDefense(actorId,jsonObject))
                .reduce(0,(a,b) -> a + b);
    }

    public abstract int computeDefense(long actorId,JSONObject jsonObject);

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
