package com.linlazy.mmorpg.template.skill;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 攻击力为A,造成可以攻击B人，具有C秒冷却时间,消耗D点MP
 * @author linlazy
 */
@Component
public  class SkillTemplate3 extends BaseSkillTemplate {

    @Override
    protected int skillTemplateId() {
        return 3;
    }

    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill, JSONObject jsonObject) {
        JSONObject skillTemplateArgs = skill.getSkillTemplateArgs();
        //技能冷却，消耗MP
        int cd = skillTemplateArgs.getIntValue("cd");
        int consumeMP = skillTemplateArgs.getIntValue("mp");
        sceneEntity.consumeMP(consumeMP);
        skill.setNextCDResumeTimes(DateUtils.getNowMillis() + cd * 1000);


        int attack = skillTemplateArgs.getIntValue("attack");
        Set<SceneEntity> targetSceneEntitySet = (Set<SceneEntity>) jsonObject.get("targetSceneEntitySet");
        targetSceneEntitySet.stream()
                .forEach(sceneEntity1 -> {
                    sceneEntity1.attacked(sceneEntity,attack);
                });
    }
}
