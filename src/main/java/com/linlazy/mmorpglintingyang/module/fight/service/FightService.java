package com.linlazy.mmorpglintingyang.module.fight.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.service.attackmode.BaseAttackMode;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class FightService {

    public Result<?> attack(long actorId, JSONObject jsonObject) {
        int attackMode = jsonObject.getIntValue("attackMode");
        return BaseAttackMode.getAttackMode(attackMode).attack(actorId,jsonObject);
    }
}
