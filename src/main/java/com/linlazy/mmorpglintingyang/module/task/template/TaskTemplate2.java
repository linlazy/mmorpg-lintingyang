package com.linlazy.mmorpglintingyang.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 *  当前金币达到xxx
 */
public class TaskTemplate2 extends TaskTemplate {
    /**
     * 玩家金币改变事件
     * @return
     */
    @Override
    public Set<EventType> likeEvent() {
        return Sets.newHashSet(EventType.ACTOR_GOLD_CHANGE);
    }

    @Override
    protected int templateId() {
        return 2;
    }

    @Autowired
    private UserManager userManager;

    /**
     * 是否达成任务条件
     * @param actorId
     * @param taskDo
     * @return
     */
    @Override
    public boolean isReachCondition(long actorId, TaskDo taskDo) {
        JSONObject data = taskDo.getData();
        int gold = data.getIntValue("gold");
        return userManager.getUser(actorId).getGold() >= gold;
    }
}