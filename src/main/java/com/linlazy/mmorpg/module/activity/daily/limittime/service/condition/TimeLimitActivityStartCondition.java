package com.linlazy.mmorpg.module.activity.daily.limittime.service.condition;

import com.linlazy.mmorpg.module.activity.daily.limittime.constants.ActivityStatus;
import com.linlazy.mmorpg.module.activity.daily.limittime.domain.LimitTimeActivity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *
 * @author linlazy
 */
@Component
public class TimeLimitActivityStartCondition extends LimitActivityStartCondition{

    @Override
    protected Integer startType() {
        return LimitActivityStartConditionType.TIME;
    }

    @Override
    public boolean isReachCondition(LimitTimeActivity limitTimeActivity) {
        if(limitTimeActivity.getStatus() != ActivityStatus.UN_START){
            return true;
        }
        return LocalDateTime.now().isAfter( limitTimeActivity.getStartTime());
    }

}
