package com.linlazy.mmorpg.module.fight.attack.actor;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.skill.config.SkillConfigService;
import com.linlazy.mmorpg.module.skill.dao.SkillDAO;
import com.linlazy.mmorpg.module.skill.entity.SkillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 技能攻击力 = 基础攻击力 + 技能等级 * 6
 * @author linlazy
 */
@Component
public class SkillActorAttack extends BaseActorAttack {

    @Autowired
    private SkillConfigService skillConfigService;
    @Autowired
    private SkillDAO skillDao;

    @Override
    public int attackType() {
        return AttackType.SKILL;
    }

    @Override
    public boolean isValid(long actorId,JSONObject jsonObject){
        int skillId = jsonObject.getIntValue("skillId");
        if( skillId == 0){
            return false;
        }

        SkillEntity skillEntity = skillDao.getSkill(actorId, skillId);
        if(!skillEntity.isDressed()){
            return false;
        }
        return true;
    }

    @Override
    public int computeAttack(long actorId, JSONObject jsonObject) {
        int skillId = jsonObject.getIntValue("skillId");
        SkillEntity skillEntity = skillDao.getSkill(actorId, skillId);
        JSONObject skillConfig = skillConfigService.getSkillConfig(skillId);
        return skillConfig.getIntValue("attack") + skillEntity.getLevel() * 6;
    }
}
