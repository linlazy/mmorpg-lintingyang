package com.linlazy.mmorpg.template.skill.strategy;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.constants.SkillType;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.template.skill.BaseSkillTemplate;
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
