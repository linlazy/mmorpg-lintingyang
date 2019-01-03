package com.linlazy.mmorpglintingyang.module.fight.service.canattacked;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class BaseCanAttacked {

    private static Map<Integer, BaseCanAttacked> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(entityType(),this);
    }

    /**
     * 场景实体类型
     * @return  返回场景实体类型（NPC,怪物，玩家）
     */
    protected abstract int entityType();

    public static BaseCanAttacked getCanAttacked(int entityType){
        return map.get(entityType);
    }

    /**
     * 能否被攻击
     * @param actorId 玩家ID
     * @param jsonObject 可变参数
     * @return 返回能否被攻击的结果
     */
    public abstract Result<?> canAttacked(long actorId, JSONObject jsonObject);

}
