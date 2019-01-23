package com.linlazy.mmorpg.template.skill.strategy.recovery;

import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 小怪恢复技能
 * @author linlazy
 */
@Component
public class MonsterRecoverySkill extends RecoverySkill {
    @Override
    protected Integer sceneEntityType() {
        return SceneEntityType.MONSTER;
    }
    /**
     * 恢复对象为小怪或Boss
     * @param sceneEntity 使用技能对象
     * @param skill 使用技能
     * @param allSceneEntity 同场景对象
     * @return
     */
    @Override
    public Set<SceneEntity> selectRecoverySceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {

        Set<SceneEntity> attackedSceneEntitySet = new HashSet<>();
        for(SceneEntity targetSceneEntity :allSceneEntity) {
            if(targetSceneEntity.getSceneEntityType() == SceneEntityType.BOSS
                || targetSceneEntity.getSceneEntityType() == SceneEntityType.MONSTER
                ){
                    attackedSceneEntitySet.add(targetSceneEntity);
                }
        }

        return attackedSceneEntitySet;
    }
}
