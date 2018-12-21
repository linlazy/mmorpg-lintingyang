package com.linlazy.mmorpglintingyang.module.skill.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.module.skill.manager.SkillManager;
import com.linlazy.mmorpglintingyang.module.skill.validator.SkillValidator;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkillService {

    @Autowired
    private SkillValidator skillValidator;
    @Autowired
    private SkillDao skillDao;
    @Autowired
    private SkillManager skillManager;

    public Result<?> dressSkill(long actorId, int skillId, JSONObject jsonObject) {

        //未拥有此技能或此技能已穿戴
        if(skillValidator.notHas(actorId,skillId) || skillValidator.isDressed(actorId,skillId)){
            return Result.valueOf("参数错误");
        }

        return skillManager.dressSkill(actorId,skillId);
    }

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
