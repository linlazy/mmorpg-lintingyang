package com.linlazy.mmorpg.module.activity.daily.everyday.domain;

import com.linlazy.mmorpg.entity.DailyActivityEntity;
import lombok.Data;

import java.util.List;

/**
 * 日常活动领域类
 * @author linlazy
 */
@Data
public class DailyActivity {

    /**
     * 玩家ID
     */
    private long actorId;


    /**
     * 当前活跃度
     */
    private int currentActivity;

    /**
     * 已领过活跃度列表
     */

    private List<Integer> rewardedActivityList;

    /**
     * 下一次重置时间
     */
    private long nextResetTime;

    public DailyActivity(DailyActivityEntity dailyActivityEntity) {
        actorId = dailyActivityEntity.getActorId();
        rewardedActivityList = dailyActivityEntity.getRewardedActivityList();
        nextResetTime = dailyActivityEntity.getNextResetTime();
    }


    public DailyActivityEntity convertDailyActivityEntity(){
        DailyActivityEntity dailyActivityEntity = new DailyActivityEntity();

        dailyActivityEntity.setActorId(actorId);
        dailyActivityEntity.setCurrentActivity(currentActivity);
        dailyActivityEntity.setRewardedActivityList(rewardedActivityList);

        return dailyActivityEntity;
    }

}
