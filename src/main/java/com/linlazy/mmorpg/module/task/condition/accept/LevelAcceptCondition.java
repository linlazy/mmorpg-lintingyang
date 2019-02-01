package com.linlazy.mmorpg.module.task.condition.accept;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.module.task.domain.TriggerCondition;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 接受任务条件：等级
 * @author linlazy
 */
public class LevelAcceptCondition extends AcceptCondition{
    @Override
    protected Integer acceptConditionType() {
        return AcceptConditionType.LEVEL;
    }

    @Autowired
    private PlayerService playerService;

    @Override
    public boolean isReachCondition(long actorId, Task task) {
        Player player = playerService.getPlayer(actorId);

        TriggerCondition triggerCondition = task.getAcceptConditionMap().get(acceptConditionType());
        JSONObject triggerArgs = triggerCondition.getTriggerArgs();
        int level = triggerArgs.getIntValue("level");
        return player.getLevel() >= level;
    }
}
