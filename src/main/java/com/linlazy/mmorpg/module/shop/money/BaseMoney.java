package com.linlazy.mmorpg.module.shop.money;

import com.linlazy.mmorpg.module.shop.domain.Shop;
import com.linlazy.mmorpg.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class BaseMoney {

    private static Map<Integer,BaseMoney> baseMoneyMap = new HashMap<>();

    @PostConstruct
    public void init(){
        baseMoneyMap.put(moneyType(),this);
    }

    protected abstract Integer moneyType();


    public static BaseMoney getBaseMoney(int moneyType){
        return baseMoneyMap.get(moneyType);
    }

    /**
     * 货币是否足够，由子类决定
     * @param shop
     * @return
     */
    public abstract Result<?> isEnough(Shop shop);

    /**
     * 扣除货币
     * @param shop
     * @return
     */
    public abstract Result<?> consumeMoney(Shop shop);
}
