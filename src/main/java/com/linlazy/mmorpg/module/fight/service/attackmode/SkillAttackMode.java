package com.linlazy.mmorpg.module.fight.service.attackmode;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.dao.SkillDAO;
import com.linlazy.mmorpg.entity.SkillEntity;
import com.linlazy.mmorpg.module.skill.config.SkillConfigService;
import com.linlazy.mmorpg.module.user.manager.UserManager;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class SkillAttackMode extends BaseAttackMode {

    @Autowired
    private SkillDAO skillDao;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SkillConfigService skillConfigService;

    @Override
    public void attackAfter(long actorId, JSONObject jsonObject) {
        //技能冷却
        int skillId = jsonObject.getIntValue("skillId");
        SkillEntity skillEntity = skillDao.(actorId, skillId);
        JSONObject skillConfig = skillConfigService.getSkillConfig(skillId);
        long cd = skillConfig.getIntValue("cd") * 1000;
        skillEntity.setNextCDResumeTimes(cd + DateUtils.getNowMillis());
        skillDao.updateQueue(skillEntity);
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
        SkillEntity skillEntity = skillDao.getSkill(actorId, skillId);
        if(skillEntity == null|| !skillEntity.isDressed()){
            return Result.valueOf("参数错误");
        }

        //技能冷却
        if(DateUtils.getNowMillis() < skillEntity.getNextCDResumeTimes()){
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
