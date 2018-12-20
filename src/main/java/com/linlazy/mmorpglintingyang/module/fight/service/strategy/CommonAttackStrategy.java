package com.linlazy.mmorpglintingyang.module.fight.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.Result;

public class CommonAttackStrategy extends AttackStrategy {

    @Override
    public Result<?> attack(long actorId, JSONObject jsonObject) {
        return Result.success();
    }
}
