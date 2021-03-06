package com.linlazy.mmorpg.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.scene.domain.Monster;
import com.linlazy.mmorpg.module.task.domain.Task;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 *  击杀xxx小怪N只
 * @author linlazy
 */
@Component
public class TaskTemplate3 extends BaseTaskTemplate {

    /**
     * 关心场景怪物死亡事件
     * @return
     */
    @Override
    public Set<EventType> likeEvent() {
        return Sets.newHashSet(EventType.SCENE_MONSTER_DEAD);
    }

    @Override
    protected int templateId() {
        return 3;
    }

    /**
     * 前置条件是否通过
     * @param actorId
     * @param jsonObject
     * @param task
     * @return
     */
    @Override
    public boolean isPreCondition(long actorId, JSONObject jsonObject, Task task) {
        Monster monster = (Monster) jsonObject.get("monster");
        JSONObject taskTemplateArgs = task.getTaskTemplateArgs();
        String monsterName = taskTemplateArgs.getString("monsterName");
        return monster.getName().equals(monsterName) ;
    }

    /**
     * 更新任务数据
     * @param actorId
     * @param jsonObject
     * @param task
     * @return
     */
    @Override
    public Task updateTaskData(long actorId, JSONObject jsonObject, Task task) {

        JSONObject data = task.getData();
        int count = data.getIntValue("count");
        data.put("count",count + 1);
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
        JSONObject data = task.getData();
        return data.getIntValue("count") >= task.getTaskTemplateArgs().getIntValue("targetCount");
    }


}
