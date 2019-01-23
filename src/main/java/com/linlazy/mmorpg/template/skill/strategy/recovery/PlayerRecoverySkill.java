package com.linlazy.mmorpg.template.skill.strategy.recovery;

import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 玩家恢复技能
 * @author linlazy
 */
@Component
public class PlayerRecoverySkill extends RecoverySkill {
    @Override
    protected Integer sceneEntityType() {
        return SceneEntityType.PLAYER;
    }

    /**
     * 恢复对象为自身，组队玩家
     * @param sceneEntity 使用技能对象
     * @param skill 使用技能
     * @param allSceneEntity 同场景对象
     * @return
     */
    @Override
    public Set<SceneEntity> selectRecoverySceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {
        Player player = (Player) sceneEntity;

        Set<SceneEntity> recoverySceneEntitySet = new HashSet<>();

        recoverySceneEntitySet.add(sceneEntity);

        for(SceneEntity targetSceneEntity :allSceneEntity) {
            if(targetSceneEntity.getSceneEntityType() == SceneEntityType.BOSS
                    ||targetSceneEntity.getSceneEntityType() == SceneEntityType.MONSTER) {
                continue;
            }

            if(targetSceneEntity.getSceneEntityType() ==SceneEntityType.PLAYER){
                Player targetPlayer = (Player) targetSceneEntity;
                if( player.isTeam()){
                    if(player.getTeam().getPlayerTeamInfoMap().containsKey(targetPlayer.getActorId())){
                        recoverySceneEntitySet.add(targetPlayer);
                    }
                }
            }
        }

        return recoverySceneEntitySet;
    }
}
