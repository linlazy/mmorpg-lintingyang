package com.linlazy.mmorpg.module.activity.daily.limittime.domain;

import com.linlazy.mmorpg.module.activity.daily.limittime.constants.ActivityStatus;
import com.linlazy.mmorpg.module.activity.daily.limittime.service.condition.LimitActivityStartCondition;
import com.linlazy.mmorpg.module.task.domain.TriggerCondition;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 限时活动领域类
 * @author linlazy
 */
@Data
public class LimitTimeActivity {


    /**
     * 标识
     */
    private long id;

    /**
     * 活动状态
     */
    private int status;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 开启时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     *
     */
    private Map<Integer, TriggerCondition> startConditionMap = new HashMap<>();

    /**
     * 开启
     * @return
     */
    public boolean doStart(){
        if(status > ActivityStatus.UN_START){
            return false;
        }
        Collection<TriggerCondition> triggerConditions = startConditionMap.values();
        for(TriggerCondition triggerCondition: triggerConditions){
            LimitActivityStartCondition limitActivityStartCondition = LimitActivityStartCondition.getLimitActivityStartCondition(triggerCondition.getTriggerType());
            if(!limitActivityStartCondition.isReachCondition(this)){
                return false;
            }
        }
        status = ActivityStatus.DOING;
        return true;
    }

    public void doEnd() {
        status = ActivityStatus.END;
    }
}
