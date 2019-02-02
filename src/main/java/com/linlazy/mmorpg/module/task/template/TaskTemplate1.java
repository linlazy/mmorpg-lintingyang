package com.linlazy.mmorpg.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpg.file.config.MonsterConfig;
import com.linlazy.mmorpg.file.config.SceneConfig;
import com.linlazy.mmorpg.file.service.MonsterConfigService;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.scene.domain.Monster;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 *  在场景A杀死B只C小怪
 * @author linlazy
 */
@Component
public class TaskTemplate1 extends BaseTaskTemplate {
    /**
     * 关心事件小怪死亡
     * @return
     */
    @Override
    public Set<EventType> likeEvent() {
        return Sets.newHashSet(EventType.SCENE_MONSTER_DEAD);
    }

    @Override
    protected int templateId() {
        return 1;
    }



    @Override
    public boolean isPreCondition(long actorId, JSONObject jsonObject, Task task) {
        JSONObject taskTemplateArgs = task.getTaskTemplateArgs();
        Monster monster = (Monster) jsonObject.get("monster");

        int targetSceneId = taskTemplateArgs.getIntValue("sceneId");
        if(monster.getSceneId() != targetSceneId){
            return false;
        }

        int monsterId = taskTemplateArgs.getIntValue("monsterId");
        if(monster.getMonsterId() != monsterId){
            return false;
        }
        return true;
    }

    @Override
    public Task updateTaskData(long actorId, JSONObject jsonObject, Task task) {
        JSONObject data = task.getData();
        int killedNum = data.getIntValue("killedNum");
        data.put("killedNum",killedNum + 1);
        return task;
    }

    /**
     * 是否达成任务条件
     * @param actorId
     * @param task
     * @return
     */
    @Override
    public boolean isReachCondition(long actorId, Task task) {
        JSONObject taskTemplateArgs = task.getTaskTemplateArgs();
        int killNum = taskTemplateArgs.getIntValue("killNum");
        int killedNum = task.getData().getIntValue("killedNum");
        return killedNum >= killNum;
    }

    @Override
    public String getTaskProcess(Task task) {

        JSONObject taskTemplateArgs = task.getTaskTemplateArgs();

        int sceneId = taskTemplateArgs.getIntValue("sceneId");
        int monsterId = taskTemplateArgs.getIntValue("monsterId");
        MonsterConfigService monsterConfigService = SpringContextUtil.getApplicationContext().getBean(MonsterConfigService.class);
        MonsterConfig monsterConfig = monsterConfigService.getMonsterConfig(monsterId);

        JSONObject data = task.getData();
        int killedNum = data.getIntValue("killedNum");
        int killNum = taskTemplateArgs.getIntValue("killNum");


        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        SceneConfig sceneConfig = sceneConfigService.getSceneConfig(sceneId);
        /**
         * 任务进度
         */
        return String.format("在场景【%s】 杀死小怪【%s】 【%d/%d】",sceneConfig.getName(),monsterConfig.getName(),killedNum,killNum);
    }

}
