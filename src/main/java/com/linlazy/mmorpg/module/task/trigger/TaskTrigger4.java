package com.linlazy.mmorpg.module.task.trigger;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.module.task.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 等級达到A触发任务
 * @author linlazy
 */
public class TaskTrigger4 extends BaseTaskTrigger {
    @Override
    protected int triggerType() {
        return 4;
    }

    @Autowired
    private PlayerService playerService;

    @Override
    public boolean isTrigger(Task task) {
        JSONObject triggerArgs = task.getTriggerArgs();
        int level = triggerArgs.getIntValue("level");
        Player player = playerService.getPlayer(task.getActorId());
        return player.getLevel() >= level;
    }
}
