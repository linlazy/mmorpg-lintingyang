package com.linlazy.mmorpglintingyang.module.skill.manager;

import com.linlazy.mmorpglintingyang.module.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkillManager {

    @Autowired
    private SkillDao skillDao;

    public Result<?> dressSkill(long actorId, int skillId) {
        Skill skill = skillDao.getSkill(actorId, skillId);
        skill.setDressed(true);
        skillDao.updateSkill(skill);
        return Result.success();
    }

}
