package com.linlazy.mmorpg.template.skill.strategy.attack;

import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;

import java.util.HashSet;
import java.util.Set;

/**
 * 小怪攻击技能
 * @author linlazy
 */
public class MonsterAttackSkill extends AttackSkill{
    @Override
    protected Integer sceneEntityType() {
        return SceneEntityType.MONSTER;
    }

    @Override
    public Set<SceneEntity> selectAttackedSceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {

        Set<SceneEntity> attackedSceneEntitySet = new HashSet<>();
        for(SceneEntity targetSceneEntity :allSceneEntity) {
            if(targetSceneEntity.getSceneEntityType() == SceneEntityType.PLAYER
                || targetSceneEntity.getSceneEntityType() == SceneEntityType.PLAYER_CALL
                ){
                    attackedSceneEntitySet.add(targetSceneEntity);
                }
        }

        return attackedSceneEntitySet;
    }
}
