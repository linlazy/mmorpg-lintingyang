package com.linlazy.shop.money;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMoney {

    private Map<Integer,AbstractMoney> map = new HashMap<>();


    @PostConstruct
    public void init(){
        map.put(moneyType(),this);
    }

    /**
     * 货币种类
     * @return
     */
    protected abstract int moneyType();

    public abstract void buy();
}
