package com.linlazy.mmorpglintingyang.module.fight.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.service.strategy.AttackStrategy;
import com.linlazy.mmorpglintingyang.module.fight.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.fight.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FightService {

    @Autowired
    private SkillDao skillDao;

    public Result<?> attacked(long actorId, int sourceId, JSONObject jsonObject) {
        return Result.success();
    }

    public  Result<?> dressSkill(long actorId, int skillId, JSONObject jsonObject) {

        Skill skill = skillDao.getSkill(actorId, skillId);
        if(skill == null || skill.isDressed()){
            Result.valueOf("参数错误");
        }
        skill.setDressed(true);
        skillDao.updateSkill(skill);
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

    public Result<?> gainSkill(long actorId, int skillId, JSONObject jsonObject) {
        Skill skill = skillDao.getSkill(actorId, skillId);
        if(skill != null){
           return Result.valueOf("已拥有技能");
        }

        skill = new Skill();
        skill.setActorId(actorId);
        skill.setSkillId(skillId);
        skill.setDressed(false);
        skill.setLevel(1);
        skill.setNextCDResumeTimes(DateUtils.getNowMillis());

        skillDao.addSkill(skill);
        return Result.success(skill);
    }
}
