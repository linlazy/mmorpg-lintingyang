package com.linlazy.mmorpg.module.item.effect.transport;

import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 传送基类
 * @author linlazy
 */
public abstract class BaseTransport {

    private static Map<Integer,BaseTransport> transportMap = new HashMap<>();


    @PostConstruct
    public void init(){
        transportMap.put(transportType(),this);
    }

    /**
     * 传送ID，子类决定
     * @return
     */
    protected abstract Integer transportType();


    public static BaseTransport getBaseTransport(int transportType){
        return transportMap.get(transportType);
    }

    /**
     * 传送逻辑，由子类决定
     * @param actorId 玩家ID
     * @param item 传送道具
     * @return
     */
    public abstract Result<?> doTransport(long actorId, Item item);
}
