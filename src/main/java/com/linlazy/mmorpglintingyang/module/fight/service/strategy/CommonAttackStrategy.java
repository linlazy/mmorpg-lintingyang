package com.linlazy.mmorpglintingyang.module.fight.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.fight.attack.actor.Attack;
import com.linlazy.mmorpglintingyang.module.fight.attack.actor.AttackType;
import com.linlazy.mmorpglintingyang.module.fight.defense.actor.Defense;
import com.linlazy.mmorpglintingyang.module.fight.domain.AttackedTargetSet;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonAttackStrategy extends AttackStrategy {

    @Autowired
    private UserDao userDao;
    @Autowired
    private SceneManager sceneManager;

    @Override
    protected AttackedTargetSet computeAttackedTarget(long actorId, JSONObject jsonObject) {
        AttackedTargetSet attackedTargetSet = new AttackedTargetSet();

        SceneDo sceneDo = sceneManager.getSceneDo(actorId);

        //攻击目标
        int targetId = jsonObject.getIntValue("targetId");
        SceneEntityDo sceneEntityDo = sceneDo.getSceneEntityDoSet().stream()
                .filter(sceneEntityDo1 -> sceneEntityDo1.getSceneEntityId() == targetId)
                .findAny().get();
        attackedTargetSet.setSceneEntityDos(Sets.newHashSet(sceneEntityDo));

        return attackedTargetSet;
    }

    @Override
    protected int computeDamage(long actorId, JSONObject jsonObject) {
         int finalAttack = SpringContextUtil.getApplicationContext().getBeansOfType(Attack.class).values().stream()
        .filter(attack -> attack.attackType() != AttackType.SKILL)
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
