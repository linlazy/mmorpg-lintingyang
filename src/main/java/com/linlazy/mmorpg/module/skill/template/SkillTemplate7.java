package com.linlazy.mmorpg.module.skill.template;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.scene.domain.Scene;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.module.scene.service.SceneService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
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
                .forEach(sceneEntity1 -> sceneEntity1.attacked(sceneEntity,skill));
    }

    @Override
    public void attack(SceneEntity sceneEntity, Skill skill, SceneEntity attackTarget) {
        SceneService sceneService = SpringContextUtil.getApplicationContext().getBean(SceneService.class);
        Scene sceneBySceneEntity = sceneService.getSceneBySceneEntity(sceneEntity);
        sceneBySceneEntity.getPlayerMap().values().forEach(player -> player.attacked(sceneEntity,skill));
        sceneBySceneEntity.getPlayerCallMap().values().forEach(playerCall -> playerCall.attacked(sceneEntity,skill));
    }


}
