package com.linlazy.mmorpglintingyang.module.fight.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.service.strategy.AttackStrategy;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.stereotype.Component;

@Component
public class FightService {

    public Result<?> attack(long actorId, JSONObject jsonObject) {

        int skill = jsonObject.getIntValue("skillId");
        if(skill == 0){
            //普通攻击
            AttackStrategy attackStrategy = AttackStrategy.newAttackStrategyCommon();
            return attackStrategy.attack(actorId,jsonObject);
        }else {
            //技能攻击
            AttackStrategy attackStrategy = AttackStrategy.newAttackStrategySkill();
            return attackStrategy.attack(actorId,jsonObject);
        }
    }


}
