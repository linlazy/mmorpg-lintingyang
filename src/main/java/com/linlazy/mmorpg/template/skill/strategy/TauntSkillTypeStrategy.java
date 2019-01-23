package com.linlazy.mmorpg.template.skill.strategy;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.constants.SkillType;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.service.SceneService;
import com.linlazy.mmorpg.template.skill.BaseSkillTemplate;
import com.linlazy.mmorpg.template.skill.strategy.taunt.TauntSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 嘲讽类技能策略
 * @author linlazy
 */
@Component
public class TauntSkillTypeStrategy extends SkillTypeStrategy{

    @Autowired
    private SceneService sceneService;

    @Override
    protected Integer skillType() {
        return SkillType.ATTACK;
    }

    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill) {
        Set<SceneEntity> allSceneEntity = sceneService.getAllSceneEntity(sceneEntity);
        Set<SceneEntity> targetSceneEntitySet = selectTauntedSceneEntitySet(sceneEntity,skill, allSceneEntity);
        BaseSkillTemplate skillTemplate = BaseSkillTemplate.getSkillTemplate(skill.getSkillTemplateId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("targetSceneEntitySet",targetSceneEntitySet);
        skillTemplate.useSkill(sceneEntity,skill,jsonObject);
    }

    private Set<SceneEntity> selectTauntedSceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {
        TauntSkill tauntSkill = TauntSkill.getTauntSkill(sceneEntity.getSceneEntityType());
        return tauntSkill.selectTauntedSceneEntitySet(sceneEntity,skill,allSceneEntity);
    }
}
