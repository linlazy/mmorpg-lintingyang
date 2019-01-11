package com.linlazy.mmorpg.module.skill.validator;

import com.linlazy.mmorpg.module.skill.dao.SkillDAO;
import com.linlazy.mmorpg.module.skill.entity.SkillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 技能校验器
 * @author linlazy
 */
@Component
public class SkillValidator {

    @Autowired
    private SkillDAO skillDao;

    public boolean isDressed(long actorId,int skillId){
        SkillEntity skillEntity = skillDao.getSkill(actorId, skillId);
        if(skillEntity != null && skillEntity.isDressed()){
           return true;
        }
        return false;
    }

    public boolean notHas(long actorId,int skillId){
        return skillDao.getSkill(actorId, skillId) == null;
    }
}
