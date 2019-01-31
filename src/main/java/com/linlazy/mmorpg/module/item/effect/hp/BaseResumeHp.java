package com.linlazy.mmorpg.module.item.effect.hp;

import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 恢复HP基类
 * @author linlazy
 */
public abstract class BaseResumeHp {

    private static Map<Integer, BaseResumeHp> baseHpMap = new HashMap<>();


    @PostConstruct
    public void init(){
        baseHpMap.put(resumeHpType(),this);
    }

    /**
     * 恢复方式，由子类决定
     * @return
     */
    protected abstract Integer resumeHpType();

    public static BaseResumeHp getBaseResumeHp(int resumeHpType){
        return baseHpMap.get(resumeHpType);
    }

    /**
     * 恢复hp具体逻辑，由子类决定
     * @return
     */

    public abstract Result<?> doResumeHp(long actorId, Item item);
}
