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
public class SkillAttack extends Attack{

    @Autowired
    private SkillConfigService skillConfigService;
    @Autowired
    private SkillDao skillDao;

    @Override
    public int attackType() {
        return AttackType.SKILL;
    }

    @Override
    public int computeAttack(long actorId, JSONObject jsonObject) {
        int skillId = jsonObject.getIntValue("skillId");
        Skill skill = skillDao.getSkill(actorId, skillId);
        JSONObject skillConfig = skillConfigService.getSkillConfig(skillId);
        return skillConfig.getIntValue("attack") + skill.getLevel() * 6;
    }
}
