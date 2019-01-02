package com.linlazy.mmorpglintingyang.module.task.template;

import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.equip.manager.domain.EquipDo;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 *  穿戴装备等级总和达到XXX
 */
@Component
public class TaskTemplate4 extends TaskTemplate {
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
    private ItemDao itemDao;

    /**
     * 是否达成任务条件
     * @param actorId
     * @param taskDo
     * @return
     */
    @Override
    public boolean isReachCondition(long actorId, TaskDo taskDo) {

        Set<Item> itemSet = itemDao.getItemSet(actorId);
        int dressEquipLevelTotal = itemSet.stream()
                .map(ItemDo::new)
                .map(EquipDo::new)
                .filter(equipDo -> equipDo.isDressed())
                .map(EquipDo::getLevel)
                .reduce(0, (a, b) -> a + b);

        return dressEquipLevelTotal >= taskDo.getTaskTemplateArgs().getIntValue("dressEquipLevelTotal");
    }


}
