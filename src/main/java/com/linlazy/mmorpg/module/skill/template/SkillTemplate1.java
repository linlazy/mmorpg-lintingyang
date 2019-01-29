package com.linlazy.mmorpg.module.skill.template;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 攻击力为A,造成可以攻击B人,具有C秒冷却时间
 * @author linlazy
 */
@Component
public  class SkillTemplate1 extends BaseSkillTemplate {


    @Override
    protected int skillTemplateId() {
        return 1;
    }

    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill, JSONObject jsonObject) {

        JSONObject skillTemplateArgs = skill.getSkillTemplateArgs();
        //技能冷却
        int cd = skillTemplateArgs.getIntValue("cd");
        skill.setNextCDResumeTimes(DateUtils.getNowMillis() + cd * 1000);

        Set<SceneEntity> targetSceneEntitySet = (Set<SceneEntity>) jsonObject.get("targetSceneEntitySet");
        int attackNum = skillTemplateArgs.getIntValue("attackNum");
        Map<Boolean, Set<SceneEntity>> collectMap = targetSceneEntitySet.stream()
                .collect(groupingBy(SceneEntity::isTauntStatus,Collectors.toSet()));
        Set<SceneEntity> tauntSceneEntities = collectMap.get(true);

        if(tauntSceneEntities == null){
            Set<SceneEntity> unTauntSceneEntities = collectMap.get(false);
            for(int i = 1; i <= attackNum ; i++){
                SceneEntity targetSceneEntity = RandomUtils.randomElement(unTauntSceneEntities);
                unTauntSceneEntities.remove(targetSceneEntity);
                targetSceneEntity.attacked(sceneEntity,skill);
            }
            return;
        }else if(tauntSceneEntities.size() >= attackNum){
                tauntSceneEntities.stream()
                        .forEach(targetSceneEntity->targetSceneEntity.attacked(sceneEntity,skill));
                return;
        }else {
            tauntSceneEntities.stream()
                    .forEach(sceneEntity1 -> sceneEntity1.attacked(sceneEntity,skill));
            attackNum -=tauntSceneEntities.size();
        }

        Set<SceneEntity> unTauntSceneEntities = collectMap.get(false);
        for(int i = 1; i <= attackNum ; i++){
            SceneEntity targetSceneEntity = RandomUtils.randomElement(unTauntSceneEntities);
            unTauntSceneEntities.remove(targetSceneEntity);
            targetSceneEntity.attacked(sceneEntity,skill);
        }

    }
}
