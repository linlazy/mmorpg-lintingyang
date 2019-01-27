package com.linlazy.mmorpg.module.skill.strategy.taunt;

import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 玩家嘲讽技能
 * @author linlazy
 */
@Component
public class PlayerTauntSkill extends TauntSkill {
    @Override
    protected Integer sceneEntityType() {
        return SceneEntityType.PLAYER;
    }

    @Override
    public Set<SceneEntity> selectTauntedSceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {
        Player player = (Player) sceneEntity;

        Set<SceneEntity> attackedSceneEntitySet = new HashSet<>();

        for(SceneEntity targetSceneEntity :allSceneEntity) {
            if(targetSceneEntity.getSceneEntityType() == SceneEntityType.BOSS
                    ||targetSceneEntity.getSceneEntityType() == SceneEntityType.MONSTER) {
                attackedSceneEntitySet.add(targetSceneEntity);
                continue;
            }

            if(targetSceneEntity.getSceneEntityType() ==SceneEntityType.PLAYER){
                Player targetPlayer = (Player) targetSceneEntity;
                if(targetPlayer.getActorId() == player.getActorId()){
                    continue;
                }
                if( player.isTeam()){
                    if(!player.getTeam().getPlayerTeamInfoMap().containsKey(targetPlayer.getActorId())){
                        attackedSceneEntitySet.add(targetPlayer);
                    }
                }else {
                    attackedSceneEntitySet.add(targetPlayer);
                }
            }
        }

        return attackedSceneEntitySet;
    }
}
