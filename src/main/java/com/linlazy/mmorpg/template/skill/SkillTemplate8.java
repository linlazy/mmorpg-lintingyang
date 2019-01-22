package com.linlazy.mmorpg.template.skill;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 中毒效果,持续时间A秒,每隔B秒，掉C点HP
 * @author linlazy
 */
@Component
public class SkillTemplate8 extends BaseSkillTemplate {


    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    @Override
    protected int skillTemplateId() {
        return 8;
    }

    @Override
    public void useSkill(SceneEntity sceneEntity, Skill skill, JSONObject jsonObject) {


        JSONObject skillTemplateArgs = skill.getSkillTemplateArgs();
        int hp = skillTemplateArgs.getIntValue("decreaseHP");

        int duration = skillTemplateArgs.getIntValue("duration");
        Set<SceneEntity> targetSceneEntitySet = (Set<SceneEntity>) jsonObject.get("targetSceneEntitySet");
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                targetSceneEntitySet.stream()
                        .forEach(sceneEntity1 -> {
                            sceneEntity1.consumeHP(hp);
                        });
            }
        }, 0L, duration, TimeUnit.SECONDS);


        int continueTime = skillTemplateArgs.getIntValue("continueTime");
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                scheduledFuture.cancel(true);
            }
        },continueTime ,TimeUnit.SECONDS);
    }


}
