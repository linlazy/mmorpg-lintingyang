package com.linlazy.mmorpglintingyang.module.fight.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.equip.manager.domain.DressedEquip;
import com.linlazy.mmorpglintingyang.module.fight.domain.AttackedTargetSet;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AttackStrategy {

    @Autowired
    DressedEquip dressedEquip;


    public static AttackStrategy  newAttackStrategyCommon(){
        return SpringContextUtil.getApplicationContext().getBean(CommonAttackStrategy.class);
    }

    public static AttackStrategy newAttackStrategySkill() {
        return  SpringContextUtil.getApplicationContext().getBean(SkillAttackStrategy.class);
    }

    public final Result<?> attack(long actorId, JSONObject jsonObject){
        Result<?> result = isCanAttack(actorId,jsonObject);
        if(result.isFail()){
            return Result.valueOf(result.getCode());
        }

        //计算最终伤害值
        int damage = computeDamage(actorId,jsonObject);
        // 已穿戴装备耐久度消耗
        dressedEquip.consumeDurabilityWithAttack(actorId,damage);
        // 受攻击目标集合 血量消耗
        AttackedTargetSet attackedEntitySet = computeAttackedTarget(actorId,jsonObject);
        attackedEntitySet.attacked(damage,jsonObject);
        return Result.success();
    }

    protected abstract AttackedTargetSet computeAttackedTarget(long actorId, JSONObject jsonObject);

    protected abstract int computeDamage(long actorId, JSONObject jsonObject);


    protected   Result<?> isCanAttack(long actorId, JSONObject jsonObject){
        return Result.success();
    }

}
