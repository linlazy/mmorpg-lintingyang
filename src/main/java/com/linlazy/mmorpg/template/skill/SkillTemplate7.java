package com.linlazy.mmorpg.template.skill;

import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全体攻击
 * @author linlazy
 */
@Component
public class SkillTemplate7 extends BaseSkillTemplate {

    @Autowired
    private SceneService sceneService;

    @Override
    protected int skillTemplateId() {
        return 7;
    }



    @Override
    protected Set<SceneEntity> selectAttackedSceneEntity(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {
        Set<SceneEntity> sceneEntitySet = new HashSet<>();

        if(sceneEntity.getSceneEntityType() == SceneEntityType.PLAYER){
            Player player = (Player) sceneEntity;

            for (SceneEntity targetSceneEntity :allSceneEntity){
                //小怪，boss
                if(targetSceneEntity.getSceneEntityType() !=SceneEntityType.PLAYER){
                    sceneEntitySet.add(targetSceneEntity);
                }else {
                    // 非玩家队员
                    Player playerSceneEntity= (Player) targetSceneEntity;
                    if(player.getTeam().getPlayerTeamInfoMap().containsKey(playerSceneEntity.getActorId())){
                        sceneEntitySet.add(sceneEntity);
                    }
                }
            }

        }else if(sceneEntity.getSceneEntityType() !=SceneEntityType.PLAYER){
            Set<SceneEntity> players = allSceneEntity.stream()
                    .filter(sceneEntity1 -> sceneEntity1.getSceneEntityType() == SceneEntityType.PLAYER)
                    .filter(sceneEntity1 -> sceneEntity1.getHp() > 0 )
                    .collect(Collectors.toSet());
            sceneEntitySet.addAll(players);
        }
        return sceneEntitySet;
    }


}
