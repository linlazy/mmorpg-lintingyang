package com.linlazy.mmorpglintingyang.module.skill.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.skill.manager.SkillManager;
import com.linlazy.mmorpglintingyang.module.skill.validator.SkillValidator;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkillService {

    @Autowired
    private SkillValidator skillValidator;
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

        return skillManager.levelUp(actorId,skillId,jsonObject);
    }
}
