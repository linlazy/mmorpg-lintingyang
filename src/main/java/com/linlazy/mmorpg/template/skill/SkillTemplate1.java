package com.linlazy.mmorpg.template.skill;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.utils.DateUtils;

/**
 * 攻击力为A，具有B秒冷却时间
 * @author linlazy
 */
public  class SkillTemplate1 extends BaseSkillTemplate {


    @Override
    protected int skillTemplateId() {
        return 1;
    }

    public void useSkill(SceneEntity sceneEntity, Skill skill, JSONObject jsonObject) {
        JSONObject skillTemplateArgs = skill.getSkillTemplateArgs();
        int attack = skillTemplateArgs.getIntValue("attack");
        int skillAttack = sceneEntity.computeAttack();

        int cd = skillTemplateArgs.getIntValue("cd");
        skill.setNextCDResumeTimes(DateUtils.getNowMillis() + cd * 1000);

        SceneEntity targetSceneEntity = jsonObject.getObject("targetSceneEntity", SceneEntity.class);
        targetSceneEntity.attacked(attack+skillAttack);

    }



}
