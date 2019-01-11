package com.linlazy.mmorpg.module.backpack.manager;

import com.alibaba.fastjson.JSONArray;
import com.linlazy.mmorpg.module.item.manager.backpack.domain.BackPack;
import org.springframework.stereotype.Component;

/**
 * 背包管理类
 * @author linlazy
 */
@Component
public class BackPackManager {

    /**
     * 是否背包已满
     * @param actorId
     * @param jsonArray
     * @return
     */
    public boolean isFullBackPack(long actorId, JSONArray jsonArray){
        return new BackPack(actorId).isFull(jsonArray);
    }
}