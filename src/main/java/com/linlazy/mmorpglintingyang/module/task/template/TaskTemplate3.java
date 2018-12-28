package com.linlazy.mmorpglintingyang.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;

import java.util.Set;

/**
 *  击杀特定小怪N只
 */
public class TaskTemplate3 extends TaskTemplate {
    @Override
    public Set<EventType> likeEvent() {
        return Sets.newHashSet(EventType.SCENE_MONSTER_DEAD);
    }

    @Override
    public boolean isPreCondition(long actorId, JSONObject jsonObject, TaskDo taskDo) {
        int entityId = jsonObject.getIntValue("entityId");
        JSONObject data = taskDo.getData();
        int monsterId = data.getIntValue("monsterId");
        return entityId == monsterId;
    }

    @Override
    public TaskDo updateTaskData(long actorId,JSONObject jsonObject, TaskDo taskDo) {
        int increaseCount = jsonObject.getIntValue("num");

        JSONObject data = taskDo.getData();
        int count = data.getIntValue("count");
        data.put("count",count + increaseCount);
        return taskDo;
    }

    @Override
    public boolean isReachCondition(long actorId, TaskDo taskDo) {
        JSONObject data = taskDo.getData();
        return data.getIntValue("count") >= taskDo.getTaskTemplateArgs().getIntValue("targetCount");
    }


}
