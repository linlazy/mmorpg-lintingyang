package com.linlazy.mmorpglintingyang.module.fight.service.canattacked;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class CanAttacked {

    private static Map<Integer, CanAttacked> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(entityType(),this);
    }

    protected abstract int entityType();

    public static CanAttacked getCanAttacked(int entityType){
        return map.get(entityType);
    }

    public abstract Result<?> canAttacked(long actorId, JSONObject jsonObject);

}
