package com.linlazy.mmorpg.template.skill;


import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.utils.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 攻击力为A，具有B秒冷却时间
 * @author linlazy
 */
@Component
public  class SkillTemplate1 extends BaseSkillTemplate {


    @Override
    protected int skillTemplateId() {
        return 1;
    }

    @Override
    protected Set<SceneEntity> selectAttackedSceneEntity(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {
        Set<SceneEntity> result = new HashSet<>();

        if(sceneEntity.getSceneEntityType() == SceneEntityType.PLAYER){

            Set<SceneEntity> sceneEntitySet = new HashSet<>();

            Player player = (Player) sceneEntity;

            for (SceneEntity targetSceneEntity :allSceneEntity){
                //小怪，boss
                if(targetSceneEntity.getSceneEntityType() !=SceneEntityType.PLAYER){
                    sceneEntitySet.add(targetSceneEntity);
                }else {
                    // 非玩家队员
                    if( player.isTeam()){
                        Player playerSceneEntity= (Player) targetSceneEntity;
                        if(!player.getTeam().getPlayerTeamInfoMap().containsKey(playerSceneEntity.getActorId())){
                            sceneEntitySet.add(sceneEntity);
                        }
                    }

                }
            }

            if(sceneEntitySet.size() > 0){
                SceneEntity sceneEntity1 = RandomUtils.randomElement(sceneEntitySet);
                result.add(sceneEntity1);
            }

        }else if(sceneEntity.getSceneEntityType() !=SceneEntityType.PLAYER){
            Set<SceneEntity> players = allSceneEntity.stream()
                    .filter(sceneEntity1 -> sceneEntity1.getSceneEntityType() == SceneEntityType.PLAYER)
                    .filter(sceneEntity1 -> sceneEntity1.getHp() > 0 )
                    .collect(Collectors.toSet());

            if(players.size() >0){
                SceneEntity sceneEntity1 = RandomUtils.randomElement(players);
                result.add(sceneEntity1);
            }
        }
        return result;
    }
}
