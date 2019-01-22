package com.linlazy.mmorpg.template.skill;


import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerCall;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.service.PlayerService;
import com.linlazy.mmorpg.utils.RandomUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

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


        Set<SceneEntity> attackedSceneEntitySet = new HashSet<>();
        for(SceneEntity targetSceneEntity :allSceneEntity){
            if(sceneEntity.getSceneEntityType() == SceneEntityType.PLAYER_CALL){
                PlayerCall playerCall = (PlayerCall) sceneEntity;
                long sourceId = playerCall.getSourceId();
                PlayerService playerService = SpringContextUtil.getApplicationContext().getBean(PlayerService.class);
                Player player = playerService.getPlayer(sourceId);


                if(targetSceneEntity.getSceneEntityType() == SceneEntityType.BOSS
                ||targetSceneEntity.getSceneEntityType() == SceneEntityType.MONSTER) {
                    attackedSceneEntitySet.add(targetSceneEntity);
                    continue;
                }

                if(targetSceneEntity.getSceneEntityType() ==SceneEntityType.PLAYER){
                    //攻击技能跳过自身
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

            if(sceneEntity.getSceneEntityType() == SceneEntityType.PLAYER){
                Player player = (Player) sceneEntity;
                if(targetSceneEntity.getSceneEntityType() == SceneEntityType.BOSS
                        ||targetSceneEntity.getSceneEntityType() == SceneEntityType.MONSTER) {
                    attackedSceneEntitySet.add(targetSceneEntity);
                    continue;
                }

                if(targetSceneEntity.getSceneEntityType() ==SceneEntityType.PLAYER){
                    //攻击技能跳过自身
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

            if(sceneEntity.getSceneEntityType() == SceneEntityType.MONSTER||
                sceneEntity.getSceneEntityType() == SceneEntityType.BOSS
            ){
                if(targetSceneEntity.getSceneEntityType() == SceneEntityType.PLAYER
                || targetSceneEntity.getSceneEntityType() == SceneEntityType.PLAYER_CALL
                ){
                    attackedSceneEntitySet.add(targetSceneEntity);
                }
            }
        }

        SceneEntity sceneEntity1 = RandomUtils.randomElement(attackedSceneEntitySet);

        result.add(sceneEntity1);

        return result;
    }
}
