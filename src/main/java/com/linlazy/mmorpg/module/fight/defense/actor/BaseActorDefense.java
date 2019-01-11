package com.linlazy.mmorpg.module.fight.defense.actor;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.fight.defense.Defense;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class BaseActorDefense extends Defense {

    private static Map<Integer, BaseActorDefense> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(actorDefenseType(),this);
    }

    /**
     *  返回玩家增加防御的类型（装备，等级）
     * @return 返回玩家增加防御的类型（装备，等级）
     */
    public abstract int actorDefenseType();

    public static int computeFinalDefense(long actorId, JSONObject jsonObject) {
        return map.values().stream()
                .filter(baseActorDefense -> baseActorDefense.isValid(actorId,jsonObject))
                .map(defense -> defense.computeDefense(actorId,jsonObject))
                .reduce(0,(a,b) -> a + b);
    }

    /**
     * 计算玩家防御力
     * @param actorId 玩家ID
     * @param jsonObject 可变参数
     * @return 返回防御力
     */
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
