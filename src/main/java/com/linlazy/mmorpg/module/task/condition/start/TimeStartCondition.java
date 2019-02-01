package com.linlazy.mmorpg.module.task.condition.start;

import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.module.task.domain.TriggerCondition;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 开启任务条件：时间段内
 * @author linlazy
 */
@Component
public class TimeStartCondition extends StartCondition{
    @Override
    protected Integer startConditionType() {
        return StartConditionType.TIME;
    }

    @Override
    public boolean isReachCondition(long actorId, Task task) {
        TriggerCondition triggerCondition = task.getStartConditionMap().get(startConditionType());

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
