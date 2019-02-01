package com.linlazy.mmorpg.module.item.type.consume.mp;

import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 恢复MP基类
 * @author linlazy
 */
public abstract class BaseResumeMp {

    private static Map<Integer, BaseResumeMp> baseMpMap = new HashMap<>();


    @PostConstruct
    public void init(){
        baseMpMap.put(resumeMpType(),this);
    }

    /**
     * 恢复方式，由子类决定
     * @return
     */
    protected abstract Integer resumeMpType();

    public static BaseResumeMp getBaseResumeMp(int resumeMpType){
        return baseMpMap.get(resumeMpType);
    }

    /**
     * 恢复hp具体逻辑，由子类决定
     * @return
     */

    public abstract Result<?> doResumeMp(long actorId, Item item);
}
