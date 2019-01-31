package com.linlazy.mmorpg.module.item.type;

import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */

public abstract class BaseItem {

    private static Map<Integer,BaseItem> itemMap = new HashMap<>();


    @PostConstruct
    public void init(){
        itemMap.put(itemType(),this);
    }

    /**
     * 道具类型，有子类决定
     * @return
     */
    protected abstract Integer itemType();


    /**
     * 通过道具类型获取道具
     * @param itemType
     * @return
     */
    public static BaseItem getBaseItem(int itemType){
        return itemMap.get(itemType);
    }

    /**
     * 使用道具
     * @param item
     * @return
     */
    public abstract Result<?> useItem(long actorId,Item item);
}
