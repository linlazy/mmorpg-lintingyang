package com.linlazy.mmorpg.template.task;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpg.domain.Task;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.service.PlayerService;
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
    private PlayerService playerService;

    @Override
    public boolean isReachCondition(long actorId, Task task) {
        JSONObject taskTemplateArgs = task.getTaskTemplateArgs();
        int level = taskTemplateArgs.getIntValue("level");
        return playerService.getPlayer(actorId).getLevel() >= level;
    }
}
