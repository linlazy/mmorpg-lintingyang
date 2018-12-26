package com.linlazy.mmorpglintingyang.module.fight.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.fight.attack.actor.Attack;
import com.linlazy.mmorpglintingyang.module.fight.defense.actor.Defense;
import com.linlazy.mmorpglintingyang.module.fight.domain.AttackedTargetSet;
import com.linlazy.mmorpglintingyang.module.fight.validator.FightValidtor;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.ScenePlayerDo;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.skill.config.SkillConfigService;
import com.linlazy.mmorpglintingyang.module.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.DateUtils;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
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

    @Autowired
    private FightValidtor fightValidtor;

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


        long targetId = jsonObject.getLongValue("targetId");
        if(targetId != 0){
            if(fightValidtor.isDifferentScene(actorId,targetId)){
                return Result.valueOf("处于不同场景");
            }
            if(!SessionManager.isOnline(targetId)){
                return Result.valueOf("被攻击玩家不在线");
            }
        }
        return Result.success();
    }

    @Override
    protected AttackedTargetSet computeAttackedTarget(long actorId, JSONObject jsonObject) {
        AttackedTargetSet attackedTargetSet = new AttackedTargetSet();

        SceneDo sceneDo = sceneManager.getSceneDo(actorId);

        //攻击目标
        int targetId = jsonObject.getIntValue("targetId");
        User user = userDao.getUser(targetId);
        SceneEntityDo sceneEntityDo = new SceneEntityDo(new ScenePlayerDo(user));
        attackedTargetSet.setSceneEntityDos(Sets.newHashSet(sceneEntityDo));

        return attackedTargetSet;
    }

    @Override
    protected int computeDamage(long actorId, JSONObject jsonObject) {
        int finalAttack = SpringContextUtil.getApplicationContext().getBeansOfType(Attack.class).values().stream()
                .map(attack -> attack.computeAttack(actorId,jsonObject))
                .reduce(0,(a,b) -> a + b);

        long targetId = jsonObject.getLongValue("targetId");
        int finalDefense = SpringContextUtil.getApplicationContext().getBeansOfType(Defense.class).values().stream()
                .map(defense -> defense.computeDefense(targetId))
                .reduce(0,(a,b) -> a + b);

        int damage = finalAttack - finalDefense;
        return damage <= 0 ? 1:damage;
    }

}
