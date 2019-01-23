package com.linlazy.mmorpg.template.skill.strategy.taunt;

import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;

import java.util.HashSet;
import java.util.Set;

/**
 * 玩家嘲讽技能
 * @author linlazy
 */
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
