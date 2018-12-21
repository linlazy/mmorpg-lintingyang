package com.linlazy.mmorpglintingyang.module.fight.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.service.strategy.AttackStrategy;
import com.linlazy.mmorpglintingyang.module.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FightService {

    @Autowired
    private SkillDao skillDao;

    public Result<?> attacked(long actorId, int sourceId, JSONObject jsonObject) {
        return Result.success();
    }


    public Result<?> attack(long actorId, JSONObject jsonObject) {

        int skill = jsonObject.getIntValue("skillId");
        if(skill == 0){
            AttackStrategy attackStrategy = AttackStrategy.newAttackStrategyCommon();
            return attackStrategy.attack(actorId,jsonObject);
        }else {
            AttackStrategy attackStrategy = AttackStrategy.newAttackStrategySkill();
            return attackStrategy.attack(actorId,jsonObject);
        }
    }


}
