package com.linlazy.mmorpg.module.skill.strategy;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.skill.constants.SkillType;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.module.scene.service.SceneService;
import com.linlazy.mmorpg.module.skill.template.BaseSkillTemplate;
import com.linlazy.mmorpg.module.skill.strategy.recovery.RecoverySkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 恢复类技能策略
 * @author linlazy
 */
@Component
public class RecoverySkillTypeStrategy extends SkillTypeStrategy{

    @Autowired
    private SceneService sceneService;

    @Override
    protected Integer skillType() {
        return SkillType.RECOVERY;
    }

    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill) {
        Set<SceneEntity> allSceneEntity = sceneService.getAllSceneEntity(sceneEntity);
        Set<SceneEntity> targetSceneEntitySet = selectRecoverySceneEntitySet(sceneEntity,skill, allSceneEntity);
        BaseSkillTemplate skillTemplate = BaseSkillTemplate.getSkillTemplate(skill.getSkillTemplateId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("targetSceneEntitySet",targetSceneEntitySet);
        skillTemplate.useSkill(sceneEntity,skill,jsonObject);
    }

    private Set<SceneEntity> selectRecoverySceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {
        RecoverySkill recoverySkill = RecoverySkill.getRecoverySkill(sceneEntity.getSceneEntityType());
        return recoverySkill.selectRecoverySceneEntitySet(sceneEntity,skill,allSceneEntity);
    }
}
