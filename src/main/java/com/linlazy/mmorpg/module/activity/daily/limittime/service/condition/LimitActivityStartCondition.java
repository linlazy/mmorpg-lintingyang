package com.linlazy.mmorpg.module.activity.daily.limittime.service.condition;

import com.linlazy.mmorpg.module.activity.daily.limittime.domain.LimitTimeActivity;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class LimitActivityStartCondition {

    private static Map<Integer, LimitActivityStartCondition> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(startType(),this);
    }

    /**
     * 开启类型，由子类决定
     * @return
     */
    protected abstract Integer startType();


    public static LimitActivityStartCondition getLimitActivityStartCondition(int startType){
        return map.get(startType);
    }

    /**
     * 由子类决定
     * @param limitTimeActivity
     * @return
     */
    public abstract boolean isReachCondition(LimitTimeActivity limitTimeActivity);

}
