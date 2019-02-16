package com.linlazy.mmorpg.module.activity.daily.limittime.service;

import com.linlazy.mmorpg.module.activity.daily.limittime.constants.ActivityStatus;
import com.linlazy.mmorpg.module.activity.daily.limittime.domain.LimitTimeActivity;
import com.linlazy.mmorpg.module.activity.push.ActivityPushHelper;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限时活动服务类
 * @author linlazy
 */
@Component
public class LimitTimeActivityService {


    Map<Integer, LimitTimeActivity> limitTimeActivityMap =new ConcurrentHashMap<>();

    /**
     * 开启活动，客户端主动调用，减少调度
     * @param actorId
     * @param limitTimeActivityId
     * @return
     */
    public Result<?> doStart(long actorId, int limitTimeActivityId){
        LimitTimeActivity limitTimeActivity = limitTimeActivityMap.get(limitTimeActivityId);
        boolean started = limitTimeActivity.doStart();
        if(started){
            ActivityPushHelper.pushStart(actorId,String.format("活动【%s】已开启",limitTimeActivity.getActivityName()));
        }
        return Result.success();
    }

    /**
     * 结束活动
     * @param actorId
     * @param limitTimeActivityId
     * @return
     */
    public Result<?> doEnd(long actorId, int limitTimeActivityId){
        LimitTimeActivity limitTimeActivity = limitTimeActivityMap.get(limitTimeActivityId);
        if(LocalDateTime.now().isBefore(limitTimeActivity.getEndTime())){
            return Result.valueOf("活动未结束");
        }
        limitTimeActivity.doEnd();
        ActivityPushHelper.pushStart(actorId,String.format("活动【%s】已结束",limitTimeActivity.getActivityName()));
        return Result.success();
    }

    /**
     * 进入活动
     * @param actorId
     * @return
     */
    public Result<?> enter(long actorId,int limitTimeActivityId){
        LimitTimeActivity limitTimeActivity = limitTimeActivityMap.get(limitTimeActivityId);
        if(limitTimeActivity.getStatus() != ActivityStatus.DOING){
            return Result.valueOf("活动未在进行");
        }
        return Result.success();
    }

}
