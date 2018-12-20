package com.linlazy.mmorpglintingyang.module.fight.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.skill.config.SkillConfigService;
import com.linlazy.mmorpglintingyang.module.fight.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.fight.skill.domain.DressSkill;
import com.linlazy.mmorpglintingyang.module.fight.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkillAttackStrategy extends AttackStrategy {

    @Autowired
    private SkillDao skillDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SkillConfigService skillConfigService;


    @Override
    public Result<?> attack(long actorId, JSONObject jsonObject) {
        int skillId = jsonObject.getIntValue("skillId");
        Skill skill = skillDao.getSkill(actorId, skillId);
        if(skill == null){
           return Result.valueOf("参数错误");
        }

        //技能冷却
        if(DateUtils.getNowMillis() < skill.getNextCDResumeTimes()){
            return Result.valueOf("技能CD中...");
        }

        //是否蓝足够
        JSONObject skillConfig = skillConfigService.getSkillConfig(skillId);
        int consumeMP = skillConfig.getIntValue("mp");
        User user = userDao.getUser(actorId);
        if(user.getMp() < consumeMP){
            return Result.valueOf("mp不足");
        }


        new DressSkill(actorId).attackMonsterWith(skillId,jsonObject.getIntValue("monsterId"));

        return Result.success();
    }
}
