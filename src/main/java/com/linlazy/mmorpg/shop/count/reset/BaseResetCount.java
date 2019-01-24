package com.linlazy.mmorpg.shop.count.reset;

import com.linlazy.mmorpg.domain.Shop;
import com.linlazy.mmorpg.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 重置类型
 * @author linlazy
 */
public abstract class BaseResetCount {

    private static Map<Integer, BaseResetCount> map = new HashMap<>();


    @PostConstruct
    public void init(){
        map.put(resetType(),this);
    }

    /**
     * 重置类型，由子类决定
     * @return
     */
    protected abstract Integer resetType();


    public static BaseResetCount getBaseResetCount(int resetType){
        return map.get(resetType);
    }

    /**
     * 执行重置，由子类决定
     * @param shop
     * @return
     */
    public abstract Result<?> doReset(Shop shop);

}
