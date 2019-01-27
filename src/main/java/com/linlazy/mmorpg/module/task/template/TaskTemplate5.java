package com.linlazy.mmorpg.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.module.common.event.EventType;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 *  通过某个副本
 * @author linlazy
 */
@Component
public class TaskTemplate5 extends BaseTaskTemplate {
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
    public boolean isPreCondition(long actorId, JSONObject jsonObject, Task task) {
        int copyId = jsonObject.getIntValue("copyId");
        return copyId == task.getTaskTemplateArgs().getIntValue("copyId");
    }

    /**
     * 是否达成任务条件
     * @param actorId
     * @param task
     * @return
     */
    @Override
    public boolean isReachCondition(long actorId, Task task) {
        return true;
    }


}
