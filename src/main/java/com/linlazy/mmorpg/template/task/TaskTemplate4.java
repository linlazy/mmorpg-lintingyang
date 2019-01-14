package com.linlazy.mmorpg.template.task;

import com.google.common.collect.Sets;
import com.linlazy.mmorpg.domain.Equip;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.Task;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 *  穿戴装备等级总和达到XXX
 * @author linlazy
 */
@Component
public class TaskTemplate4 extends BaseTaskTemplate {
    /**
     * 关心任务触发，穿戴装备事件
     * @return
     */
    @Override
    public Set<EventType> likeEvent() {
        return Sets.newHashSet(EventType.DRESS_EQUIP,EventType.TASK_TRIGGER);
    }

    @Override
    protected int templateId() {
        return 4;
    }

    @Autowired
    private PlayerService playerService;

    /**
     * 是否达成任务条件
     * @param actorId
     * @param task
     * @return
     */
    @Override
    public boolean isReachCondition(long actorId, Task task) {

        Player player = playerService.getPlayer(actorId);
        int dressEquipLevelTotal = player.getDressedEquip().getEquipMap().values()
                .stream()
                .map(Equip::getLevel)
                .reduce(0, (a, b) -> a + b);

        return dressEquipLevelTotal >= task.getTaskTemplateArgs().getIntValue("dressEquipLevelTotal");
    }


}
