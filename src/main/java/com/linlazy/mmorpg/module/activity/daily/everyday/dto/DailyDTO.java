package com.linlazy.mmorpg.module.activity.daily.everyday.dto;

import com.linlazy.mmorpg.module.activity.daily.everyday.domain.DailyActivity;

import java.util.List;

/**
 * 客户端所需数据
 * @author linlazy
 */
public class DailyDTO {

    /**
     * 当前活跃度
     */
    private Integer currentActivity;
    /**
     * 已领取活跃度列表
     */
    private List<Integer> rewardedList;

    public DailyDTO(DailyActivity playerDailyActivity) {
        currentActivity = playerDailyActivity.getCurrentActivity();
        rewardedList = playerDailyActivity.getRewardedActivityList();
    }
}
