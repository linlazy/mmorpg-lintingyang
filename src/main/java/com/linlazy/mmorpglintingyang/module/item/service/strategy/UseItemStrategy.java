package com.linlazy.mmorpglintingyang.module.item.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;

public abstract class UseItemStrategy {
    public static UseItemStrategy newUseItemStrategy(int useItem) {

        switch (useItem){
            case 1:
                return SpringContextUtil.getApplicationContext().getBean(UseItemStrategy0.class);
            default:
                return SpringContextUtil.getApplicationContext().getBean(UseItemStrategy0.class);
        }
    }

    public abstract Result<?> doUseItem(long actorId, long itemId, JSONObject jsonObject);
}
