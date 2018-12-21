package com.linlazy.mmorpglintingyang.module.skill.validator;

import com.linlazy.mmorpglintingyang.module.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 技能校验器
 */
@Component
public class SkillValidator {

    @Autowired
    private SkillDao skillDao;

    public boolean isDressed(long actorId,int skillId){
        Skill skill = skillDao.getSkill(actorId, skillId);
        if(skill != null && skill.isDressed()){
           return true;
        }
        return false;
    }

    public boolean notHas(long actorId,int skillId){
        return skillDao.getSkill(actorId, skillId) == null;
    }
}
