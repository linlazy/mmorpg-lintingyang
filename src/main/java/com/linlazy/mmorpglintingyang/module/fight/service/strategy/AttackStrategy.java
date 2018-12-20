package com.linlazy.mmorpglintingyang.module.fight.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;

public abstract class AttackStrategy {

    public static AttackStrategy  newAttackStrategyCommon(){
        return SpringContextUtil.getApplicationContext().getBean(CommonAttackStrategy.class);
    }

    public static AttackStrategy newAttackStrategySkill() {
        return  SpringContextUtil.getApplicationContext().getBean(SkillAttackStrategy.class);
    }

    public abstract Result<?> attack(long actorId, JSONObject jsonObject);
}
