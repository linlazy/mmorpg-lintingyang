package com.linlazy.mmorpg.template.skill;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.service.SceneService;
import com.linlazy.mmorpg.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 瞬时回复A点HP，具有B秒冷却时间,消耗C点MP
 * @author linlazy
 */
@Component
public  class SkillTemplate2 extends BaseSkillTemplate {

    @Autowired
    private SceneService sceneService;

    @Override
    protected int skillTemplateId() {
        return 2;
    }

//    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill,JSONObject jsonObject) {
        JSONObject skillTemplateArgs = skill.getSkillTemplateArgs();
        //技能冷却，消耗MP
        int cd = skillTemplateArgs.getIntValue("cd");
        int consumeMP = skillTemplateArgs.getIntValue("mp");
        sceneEntity.consumeMP(consumeMP);
        skill.setNextCDResumeTimes(DateUtils.getNowMillis() + cd * 1000);

        //恢复HP
        int resumeHP =skillTemplateArgs .getIntValue("resumeHP");
        SceneEntity targetSceneEntity = jsonObject.getObject("targetSceneEntity", SceneEntity.class);
        targetSceneEntity.resumeHP(resumeHP);
    }

}
