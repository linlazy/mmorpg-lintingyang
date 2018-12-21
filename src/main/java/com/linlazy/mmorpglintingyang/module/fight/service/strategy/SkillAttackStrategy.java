package com.linlazy.mmorpglintingyang.module.fight.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.fight.domain.AttackedTargetSet;
import com.linlazy.mmorpglintingyang.module.skill.config.SkillConfigService;
import com.linlazy.mmorpglintingyang.module.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.skill.domain.SkillDo;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
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
    @Autowired
    private SceneManager sceneManager;

    @Override
    protected Result<?> isCanAttack(long actorId, JSONObject jsonObject) {
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
        return Result.success();
    }

    @Override
    protected AttackedTargetSet computeAttackedTarget(long actorId, JSONObject jsonObject) {
        AttackedTargetSet attackedTargetSet = new AttackedTargetSet();

        SceneDo sceneDo = sceneManager.getSceneDoByActorId(actorId);

        //攻击目标
        int targetId = jsonObject.getIntValue("targetId");
        SceneEntityDo sceneEntityDo = sceneDo.getSceneEntityDoSet().stream()
                .filter(sceneEntityDo1 -> sceneEntityDo1.getSceneEntityId() == targetId)
                .findAny().get();
        attackedTargetSet.setSceneEntityDos(Sets.newHashSet(sceneEntityDo));

        return attackedTargetSet;
    }

    @Override
    protected int computeAttack(long actorId, JSONObject jsonObject) {
        //装备伤害
        int equipAttack = dressedEquip.computeAttack(actorId);
        //技能伤害
        int skillId = jsonObject.getIntValue("skillId");
        Skill skill = skillDao.getSkill(actorId, skillId);
        SkillDo skillDo = new SkillDo(skill);
        int skillAttack = skillDo.computeFinalAttack();
        return equipAttack + skillAttack;
    }

}
