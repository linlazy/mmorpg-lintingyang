package com.linlazy.mmorpglintingyang.module.skill.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkillManager {

    @Autowired
    private SkillDao skillDao;

    /**
     * 穿戴技能
     * @param actorId
     * @param skillId
     * @return
     */
    public Result<?> dressSkill(long actorId, int skillId) {
        Skill skill = skillDao.getSkill(actorId, skillId);
        skill.setDressed(true);
        skillDao.updateSkill(skill);
        return Result.success();
    }

    /**
     * 升级技能
     * @param actorId
     * @param skillId
     * @param jsonObject
     * @return
     */
    public Result<?> levelUp(long actorId, int skillId, JSONObject jsonObject) {
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
