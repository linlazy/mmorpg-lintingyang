package com.linlazy.mmorpglintingyang.module.fight.service.attackmode;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.skill.config.SkillConfigService;
import com.linlazy.mmorpglintingyang.module.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class SkillAttackMode extends AttackMode {

    @Autowired
    private SkillDao skillDao;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SkillConfigService skillConfigService;

    @Override
    public void attackAfter(long actorId, JSONObject jsonObject) {
        //技能冷却
        int skillId = jsonObject.getIntValue("skillId");
        Skill skill = skillDao.getSkill(actorId, skillId);
        JSONObject skillConfig = skillConfigService.getSkillConfig(skillId);
        long cd = skillConfig.getIntValue("cd") * 1000;
        skill.setNextCDResumeTimes(cd + DateUtils.getNowMillis());
        skillDao.updateSkill(skill);
        //技能消耗MP

        int mp = skillConfig.getIntValue("mp");
        User user = userManager.getUser(actorId);
        user.modifyMP(- mp);
        userManager.updateUser(user);
    }


    @Override
    protected int attackMode() {
        return AttackModeType.SKILL;
    }

    @Override
    public Result<?> attack(long actorId, JSONObject jsonObject) {
        return super.attack(actorId, jsonObject);
    }

    @Override
    public Result<?> isCanAttack(long actorId, JSONObject jsonObject) {
        int skillId = jsonObject.getIntValue("skillId");
        Skill skill = skillDao.getSkill(actorId, skillId);
        if(skill == null|| !skill.isDressed()){
            return Result.valueOf("参数错误");
        }

        //技能冷却
        if(DateUtils.getNowMillis() < skill.getNextCDResumeTimes()){
            return Result.valueOf("技能CD中...");
        }

        //是否蓝足够
        JSONObject skillConfig = skillConfigService.getSkillConfig(skillId);
        int consumeMP = skillConfig.getIntValue("mp");
        User user = userManager.getUser(actorId);
        if(user.getMp() < consumeMP){
            return Result.valueOf("mp不足");
        }
        return super.isCanAttack(actorId,jsonObject);
    }


}
