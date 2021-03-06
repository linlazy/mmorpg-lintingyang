package com.linlazy.mmorpg.module.skill.strategy.attack;

import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * BOSS攻击技能
 * @author linlazy
 */
@Component
public class BossAttackSkill extends AttackSkill{
    @Override
    protected Integer sceneEntityType() {
        return SceneEntityType.BOSS;
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
