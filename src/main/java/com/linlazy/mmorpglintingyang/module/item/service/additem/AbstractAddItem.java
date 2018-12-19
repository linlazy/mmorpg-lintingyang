package com.linlazy.mmorpglintingyang.module.item.service.additem;

import com.linlazy.mmorpglintingyang.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAddItem {

    private static Map<Integer, AbstractAddItem> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(itemType(),this);
    }

    /**
     * 道具类型
     * @return
     */
    protected abstract int itemType();

    public static AbstractAddItem getAbstractItemType(int itemType){
        return map.get(itemType);
    }

    public abstract Result<?> addItem(long actorId,int baseItemId,int num);

}
