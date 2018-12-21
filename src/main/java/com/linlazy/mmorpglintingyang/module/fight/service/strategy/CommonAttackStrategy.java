package com.linlazy.mmorpglintingyang.module.fight.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.fight.domain.AttackedTargetSet;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonAttackStrategy extends AttackStrategy {

    @Autowired
    private UserDao userDao;
    @Autowired
    private SceneManager sceneManager;

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
        return equipAttack;
    }

}
