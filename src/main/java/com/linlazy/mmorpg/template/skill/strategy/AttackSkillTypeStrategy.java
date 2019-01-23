package com.linlazy.mmorpg.template.skill.strategy;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.constants.SkillType;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.service.SceneService;
import com.linlazy.mmorpg.template.skill.BaseSkillTemplate;
import com.linlazy.mmorpg.template.skill.strategy.attack.AttackSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 攻击类技能策略
 * @author linlazy
 */
@Component
public class AttackSkillTypeStrategy extends SkillTypeStrategy{

    @Autowired
    private SceneService sceneService;

    @Override
    protected Integer skillType() {
        return SkillType.ATTACK;
    }

    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill) {
        Set<SceneEntity> allSceneEntity = sceneService.getAllSceneEntity(sceneEntity);
        Set<SceneEntity> targetSceneEntitySet = selectAttackedSceneEntitySet(sceneEntity,skill, allSceneEntity);
        BaseSkillTemplate skillTemplate = BaseSkillTemplate.getSkillTemplate(skill.getSkillTemplateId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("targetSceneEntitySet",targetSceneEntitySet);
        skillTemplate.useSkill(sceneEntity,skill,jsonObject);
    }

    private Set<SceneEntity> selectAttackedSceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {

        //存活的场景实体
        Set<SceneEntity> liveSceneEntitySet = allSceneEntity.stream()
                .filter(sceneEntity1 -> sceneEntity1.getHp() > 0)
                .collect(Collectors.toSet());


        AttackSkill attackSkill = AttackSkill.getAttackSkill(sceneEntity.getSceneEntityType());
        return attackSkill.selectAttackedSceneEntitySet(sceneEntity,skill,liveSceneEntitySet);
    }
}
