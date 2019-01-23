package com.linlazy.mmorpg.template.skill;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.Scene;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.push.SkillPushHelper;
import com.linlazy.mmorpg.service.SceneService;
import com.linlazy.mmorpg.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 嘲讽技能,优先攻击使用技能的玩家,持续时间A秒，具有B秒冷却时间,消耗C点MP
 * @author linlazy
 */
@Component
public  class SkillTemplate5 extends BaseSkillTemplate {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    @Autowired
    private SceneService sceneService;

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


        sceneEntity.setTauntStatus(true);
        Scene sceneBySceneEntity = sceneService.getSceneBySceneEntity(sceneEntity);
        sceneBySceneEntity.getPlayerMap().values().stream()
                .forEach(player-> SkillPushHelper.pushUseSkill(player.getActorId(),String.format("【%s】处于嘲讽状态",sceneEntity.getName())));

        ScheduledFuture<?> tauntStatusScheduledFuture = scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                sceneEntity.setTauntStatus(false);
                sceneBySceneEntity.getPlayerMap().values().stream()
                        .forEach(player -> SkillPushHelper.pushUseSkill(player.getActorId(), String.format("【%s】嘲讽状态结束", sceneEntity.getName())));
            }
        }, continueTime, TimeUnit.SECONDS);
        sceneEntity.setTauntStatusScheduledFuture(tauntStatusScheduledFuture);
    }

}
