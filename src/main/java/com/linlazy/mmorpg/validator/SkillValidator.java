package com.linlazy.mmorpg.validator;

import com.linlazy.mmorpg.dao.SkillDAO;
import com.linlazy.mmorpg.entity.SkillEntity;
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
        SkillEntity skillEntity = skillDao.getEntityByPK(actorId, skillId);
        if(skillEntity != null && skillEntity.isDressed()){
           return true;
        }
        return false;
    }

    public boolean notHas(long actorId,int skillId){
        return skillDao.getEntityByPK(actorId, skillId) == null;
    }
}
