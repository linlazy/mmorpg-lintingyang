package com.linlazy.mmorpglintingyang.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 *  击杀xxx小怪N只
 */
@Component
public class TaskTemplate3 extends TaskTemplate {
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
     * @param taskDo
     * @return
     */
    @Override
    public boolean isPreCondition(long actorId, JSONObject jsonObject, TaskDo taskDo) {
        int entityId = jsonObject.getIntValue("entityId");
        JSONObject data = taskDo.getData();
        int monsterId = data.getIntValue("monsterId");
        return entityId == monsterId;
    }

    /**
     * 更新任务数据
     * @param actorId
     * @param jsonObject
     * @param taskDo
     * @return
     */
    @Override
    public TaskDo updateTaskData(long actorId,JSONObject jsonObject, TaskDo taskDo) {
        int increaseCount = jsonObject.getIntValue("num");

        JSONObject data = taskDo.getData();
        int count = data.getIntValue("count");
        data.put("count",count + increaseCount);
        return taskDo;
    }

    /**
     * 是否达成任务条件
     * @param actorId
     * @param taskDo
     * @return
     */
    @Override
    public boolean isReachCondition(long actorId, TaskDo taskDo) {
        JSONObject data = taskDo.getData();
        return data.getIntValue("count") >= taskDo.getTaskTemplateArgs().getIntValue("targetCount");
    }


}
