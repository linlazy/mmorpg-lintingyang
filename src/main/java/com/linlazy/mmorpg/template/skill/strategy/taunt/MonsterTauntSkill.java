package com.linlazy.mmorpg.template.skill.strategy.taunt;

import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 小怪嘲讽技能
 * @author linlazy
 */
@Component
public class MonsterTauntSkill extends TauntSkill {
    @Override
    protected Integer sceneEntityType() {
        return SceneEntityType.MONSTER;
    }

    @Override
    public Set<SceneEntity> selectTauntedSceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {

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
