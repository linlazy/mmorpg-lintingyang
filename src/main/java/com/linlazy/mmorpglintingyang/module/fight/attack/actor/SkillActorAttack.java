package com.linlazy.mmorpglintingyang.module.fight.attack.actor;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.skill.config.SkillConfigService;
import com.linlazy.mmorpglintingyang.module.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 技能攻击力 = 基础攻击力 + 技能等级 * 6
 */
@Component
public class SkillActorAttack extends ActorAttack {

    @Autowired
    private SkillConfigService skillConfigService;
    @Autowired
    private SkillDao skillDao;

    @Override
    public int attackType() {
        return AttackType.SKILL;
    }


    public boolean isValid(long actorId,JSONObject jsonObject){
        int skillId = jsonObject.getIntValue("skillId");
        if( skillId == 0){
            return false;
        }

        Skill skill = skillDao.getSkill(actorId, skillId);
        if(!skill.isDressed()){
            return false;
        }
        return true;
    }

    @Override
    public int computeAttack(long actorId, JSONObject jsonObject) {
        int skillId = jsonObject.getIntValue("skillId");
        Skill skill = skillDao.getSkill(actorId, skillId);
        JSONObject skillConfig = skillConfigService.getSkillConfig(skillId);
        return skillConfig.getIntValue("attack") + skill.getLevel() * 6;
    }
}
