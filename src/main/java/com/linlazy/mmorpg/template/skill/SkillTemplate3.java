package com.linlazy.mmorpg.template.skill;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.*;
import com.linlazy.mmorpg.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 攻击力为A,造成可以攻击B人，具有C秒冷却时间,消耗D点MP
 * @author linlazy
 */
@Component
public  class SkillTemplate3 extends BaseSkillTemplate {


    @Autowired
    private SceneService sceneService;

    @Override
    protected int skillTemplateId() {
        return 3;
    }

    public void useSkill(SceneEntity sceneEntity, Skill skill, JSONObject jsonObject) {
        JSONObject skillTemplateArgs = skill.getSkillTemplateArgs();
        int attack = skillTemplateArgs.getIntValue("attack");

    }

    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill) {
        //获取使用技能对象的攻击力
        int attack = skill.getSkillTemplateArgs().getIntValue("attack");
        //获取使用技能对象所处的可受攻击对象
        Set<SceneEntity> sceneEntitySet =attackedSceneEntitySet(sceneEntity,skill);
        for(SceneEntity attackedSceneEntity: sceneEntitySet){
            attackedSceneEntity.attacked(attack);
        }

    }

    /**
     *   受攻击的实体
     * @param sceneEntity 使用技能的场景实体对象
     * @return
     */
    public Set<SceneEntity> attackedSceneEntitySet(SceneEntity sceneEntity,Skill skill) {

        Set attackedSceneEntitySet = new HashSet();

        JSONObject skillTemplateArgs = skill.getSkillTemplateArgs();
        int attackNum = skillTemplateArgs.getIntValue("attackNum");

        if(sceneService.isCopyScene(sceneEntity.getSceneId())){
            Copy copy = sceneService.getCopy(sceneEntity.getCopyId());
            if(sceneEntity.getSceneEntityType() != SceneEntityType.PLAYER){

                Set<Player> players = copy.getPlayerCopyInfoMap().values().stream()
                        .map(PlayerCopyInfo::getPlayer)
                        .collect(Collectors.toSet());


                Set<Player> result = players.stream().limit(attackNum).collect(Collectors.toSet());
                attackedSceneEntitySet.addAll(result);
            }else {

                Collection<Monster> monsters = copy.getMonsterMap().values();
                Boss boss = copy.getBossList().get(copy.getCurrentBossIndex());
                attackedSceneEntitySet.add(boss);

                if(attackNum > 1){
                    Set<Monster> monsterSet = monsters.stream().limit(attackNum - 1).collect(Collectors.toSet());
                    attackedSceneEntitySet.addAll(monsterSet);
                }
            }
        }else {
            //todo 非副本
        }

        return attackedSceneEntitySet;
    }
}
