package com.linlazy.mmorpg.template.skill;

import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全体攻击
 * @author linlazy
 */
public class SkillTemplate7 extends BaseSkillTemplate {

    @Autowired
    private SceneService sceneService;

    @Override
    protected int skillTemplateId() {
        return 7;
    }

    /**
     *
     * @param sceneEntity 使用技能的场景实体对象
     * @param skill //场景实体使用的技能
     */
    public  void useSkill(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> sceneEntitySet) {
        //获取使用技能对象的攻击力
        int attack = skill.getSkillTemplateArgs().getIntValue("attack");
        //获取使用技能对象所处的可受攻击对象
        for(SceneEntity attackedSceneEntity: sceneEntitySet){
            attackedSceneEntity.attacked(attack);
        }
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
                    .collect(Collectors.toSet());
            sceneEntitySet.addAll(players);
        }
        return sceneEntitySet;
    }


}
