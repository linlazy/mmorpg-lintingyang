package com.linlazy.mmorpg.template.skill;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.service.CallService;
import com.linlazy.mmorpg.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 召唤召唤师协助作战,持续时间A秒，具有B秒冷却时间,消耗C点MP
 * @author linlazy
 */
public  class SkillTemplate4 extends BaseSkillTemplate {

    @Autowired
    private CallService callService;

    @Override
    protected int skillTemplateId() {
        return 4;
    }

    @Override
    protected void useCallSkill(SceneEntity sceneEntity, Skill skill) {
        JSONObject skillTemplateArgs = skill.getSkillTemplateArgs();
        int mp = skillTemplateArgs.getIntValue("mp");
        sceneEntity.consumeMP(mp);

        int cd = skillTemplateArgs.getIntValue("cd");
        skill.setNextCDResumeTimes(DateUtils.getNowMillis() + cd *1000);

        int continueTime = skillTemplateArgs.getIntValue("continueTime");
        callService.createCall(sceneEntity,continueTime);
    }

}
