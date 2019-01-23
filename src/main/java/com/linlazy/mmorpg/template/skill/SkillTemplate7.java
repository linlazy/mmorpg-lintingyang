package com.linlazy.mmorpg.template.skill;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 全体攻击
 * @author linlazy
 */
@Component
public class SkillTemplate7 extends BaseSkillTemplate {

    @Autowired
    private SceneService sceneService;

    @Override
    protected int skillTemplateId() {
        return 7;
    }

    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill, JSONObject jsonObject) {

        Set<SceneEntity> targetSceneEntitySet = (Set<SceneEntity>) jsonObject.get("targetSceneEntitySet");
        targetSceneEntitySet.stream()
                .forEach(sceneEntity1 -> {
                    sceneEntity1.attacked(sceneEntity,skill);
                });
    }


}
