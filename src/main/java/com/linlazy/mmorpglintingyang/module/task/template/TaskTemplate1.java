package com.linlazy.mmorpglintingyang.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 等级升到N级
 * @author linlazy
 */
@Component
public class TaskTemplate1 extends BaseTaskTemplate {

    @Override
    public Set<EventType> likeEvent() {
        return Sets.newHashSet(EventType.ACTOR_LEVEL_UP,EventType.TASK_TRIGGER);
    }

    @Override
    protected int templateId() {
        return 1;
    }


    @Autowired
    private UserManager userManager;

    @Override
    public boolean isReachCondition(long actorId, TaskDo taskDo) {
        JSONObject taskTemplateArgs = taskDo.getTaskTemplateArgs();
        int level = taskTemplateArgs.getIntValue("level");
        return userManager.getUser(actorId).getLevel() >= level;
    }
}
