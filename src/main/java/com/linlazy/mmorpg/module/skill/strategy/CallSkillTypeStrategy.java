package com.linlazy.mmorpg.module.skill.strategy;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.skill.constants.SkillType;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.module.skill.template.BaseSkillTemplate;
import org.springframework.stereotype.Component;

/**
 * 召唤类技能策略
 * @author linlazy
 */
@Component
public class CallSkillTypeStrategy extends SkillTypeStrategy{

    @Override
    protected Integer skillType() {
        return SkillType.CALL;
    }

    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill) {
        BaseSkillTemplate skillTemplate = BaseSkillTemplate.getSkillTemplate(skill.getSkillTemplateId());
        JSONObject jsonObject = new JSONObject();
        skillTemplate.useSkill(sceneEntity,skill,jsonObject);
    }
}
