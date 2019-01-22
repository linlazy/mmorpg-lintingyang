package com.linlazy.mmorpg.template.skill;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 嘲讽技能,优先攻击使用技能的玩家,持续时间A秒，具有B秒冷却时间,消耗C点MP
 * @author linlazy
 */
@Component
public  class SkillTemplate5 extends BaseSkillTemplate {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    @Override
    protected int skillTemplateId() {
        return 5;
    }

    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill, JSONObject jsonObject) {

        JSONObject skillTemplateArgs = skill.getSkillTemplateArgs();
        int mp = skillTemplateArgs.getIntValue("mp");
        sceneEntity.consumeMP(mp);

        int cd = skillTemplateArgs.getIntValue("cd");
        skill.setNextCDResumeTimes(DateUtils.getNowMillis() + cd *1000);

        int continueTime = skillTemplateArgs.getIntValue("continueTime");


        Set<SceneEntity> targetSceneEntitySet = (Set<SceneEntity>) jsonObject.get("targetSceneEntitySet");
        targetSceneEntitySet.stream()
                .forEach(sceneEntity1 -> {
                    sceneEntity1.setTauntStatus(true);
                });
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {

                targetSceneEntitySet.stream()
                        .forEach(sceneEntity1 -> {
                            sceneEntity1.setTauntStatus(false);
                        });
            }
        },continueTime, TimeUnit.SECONDS);

    }

}
