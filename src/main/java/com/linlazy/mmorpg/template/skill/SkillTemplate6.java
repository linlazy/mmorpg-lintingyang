package com.linlazy.mmorpg.template.skill;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.Scene;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.push.SkillPushHelper;
import com.linlazy.mmorpg.service.CallService;
import com.linlazy.mmorpg.service.SceneService;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 召唤召唤师协助作战,持续时间A秒，具有B秒冷却时间,消耗C点MP
 * @author linlazy
 */
@Component
public  class SkillTemplate6 extends BaseSkillTemplate {

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
        SceneService sceneService = SpringContextUtil.getApplicationContext().getBean(SceneService.class);
        Scene scene = sceneService.getSceneBySceneEntity(sceneEntity);
        scene.getPlayerMap().values()
                .forEach(player -> {
                    SkillPushHelper.pushUseSkill(player.getActorId(),String.format("【%s】使用了召唤技能【%s】",sceneEntity.getName(),skill.getName()));
                });
    }

    @Override
    protected Set<SceneEntity> selectAttackedSceneEntity(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {
        return null;
    }

}
