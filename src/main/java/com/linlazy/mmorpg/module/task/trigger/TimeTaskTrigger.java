package com.linlazy.mmorpg.module.task.trigger;

import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.module.task.domain.TriggerCondition;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间触发
 * @author linlazy
 */
@Component
public class TimeTaskTrigger extends BaseTaskTrigger{


    @Override
    protected int triggerType() {
        return TaskTriggerType.TIME;
    }

    @Override
    public boolean isTrigger(Task task) {
        TriggerCondition triggerCondition = task.getTriggerConditionMap().get(triggerType());

        String beginTimeStr = triggerCondition.getTriggerArgs().getString("beginTime");
        String endTimeStr = triggerCondition.getTriggerArgs().getString("endTime");

        DateTimeFormatter dateTimeFormatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime beginTime = LocalDateTime.parse(beginTimeStr, dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, dateTimeFormatter);

        if(LocalDateTime.now().isBefore(beginTime)||LocalDateTime.now().isAfter(endTime)){
            return false;
        }
        return true;
    }
}
