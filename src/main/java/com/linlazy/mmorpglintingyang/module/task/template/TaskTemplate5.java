package com.linlazy.mmorpglintingyang.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 *  通过某个副本
 */
@Component
public class TaskTemplate5 extends TaskTemplate {
    /**
     * 关心任务触发，穿戴装备事件
     * @return
     */
    @Override
    public Set<EventType> likeEvent() {
        return Sets.newHashSet(EventType.COPY_SUCCESS);
    }

    @Override
    protected int templateId() {
        return 5;
    }

    @Override
    public boolean isPreCondition(long actorId, JSONObject jsonObject, TaskDo taskDo) {
        int copyId = jsonObject.getIntValue("copyId");
        return copyId == taskDo.getTaskTemplateArgs().getIntValue("copyId");
    }

    /**
     * 是否达成任务条件
     * @param actorId
     * @param taskDo
     * @return
     */
    @Override
    public boolean isReachCondition(long actorId, TaskDo taskDo) {
        return true;
    }


}
