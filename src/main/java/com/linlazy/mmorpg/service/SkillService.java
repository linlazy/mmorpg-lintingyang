package com.linlazy.mmorpg.service;

import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.template.skill.BaseSkillTemplate;

/**
 * 技能服务类
 * @author linlazy
 */
public class SkillService {


    public void  attack(SceneEntity sceneEntity, Skill skill){
        BaseSkillTemplate skillTemplate = BaseSkillTemplate.getSkillTemplate(skill.getSkillTemplateId());
        skillTemplate.useSkill(sceneEntity,skill);
    }
}
