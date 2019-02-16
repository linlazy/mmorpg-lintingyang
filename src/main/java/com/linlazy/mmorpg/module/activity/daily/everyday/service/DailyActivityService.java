package com.linlazy.mmorpg.module.activity.daily.everyday.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpg.dao.DailyActivityDAO;
import com.linlazy.mmorpg.entity.DailyActivityEntity;
import com.linlazy.mmorpg.module.activity.daily.everyday.domain.DailyActivity;
import com.linlazy.mmorpg.module.activity.daily.everyday.dto.DailyDTO;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * 日常活动服务类
 * @author linlazy
 */
@Component
public class DailyActivityService {


    @Autowired
    private DailyActivityDAO dailyActivityDAO;

    /**
     * 玩家日常活跃缓存
     */
    public static LoadingCache<Long, DailyActivity> playerDailyActivityCache = CacheBuilder.newBuilder()
            .recordStats()
            .build(new CacheLoader<Long, DailyActivity>() {
                @Override
                public DailyActivity load(Long actorId) {


                    DailyActivityDAO dailyActivityDAO = SpringContextUtil.getApplicationContext().getBean(DailyActivityDAO.class);
                    DailyActivityEntity dailyActivityEntity = dailyActivityDAO.getEntityByPK(actorId);

                    DailyActivity dailyActivity = new DailyActivity(dailyActivityEntity);
                    return dailyActivity;
                }
            });


    /**
     * 获取玩家日常活跃
     * @param actorId
     * @return
     */
    private DailyActivity getPlayerDailyActivity(long actorId){
        try {
            DailyActivity dailyActivity = playerDailyActivityCache.get(actorId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 零点刷新
     * @return
     */
    public Result<?> refresh(long actorId){
        DailyActivity playerDailyActivity = getPlayerDailyActivity(actorId);
        if(DateUtils.getNowMillis() <= playerDailyActivity.getNextResetTime()){
            return Result.valueOf("未到刷新时间");
        }

        playerDailyActivity.setNextResetTime(DateUtils.getTomorrowMillis());
        playerDailyActivity.setCurrentActivity(0);
        playerDailyActivity.getRewardedActivityList().clear();
        dailyActivityDAO.updateQueue(playerDailyActivity.convertDailyActivityEntity());

        return Result.success();
    }


    /**
     * 日常活动信息
     * @return
     */
    public Result<?> dailyInfo(long actorId){
        refresh(actorId);

        DailyActivity playerDailyActivity = getPlayerDailyActivity(actorId);

        DailyDTO dailyDTO = new DailyDTO(playerDailyActivity);
        return Result.success(dailyDTO);
    }

    /**
     * 领取活跃度
     * @param actorId
     * @return
     */
    public Result<?> rewardActivity(long actorId,int rewardActivity){
        DailyActivity playerDailyActivity = getPlayerDailyActivity(actorId);
        if( playerDailyActivity.getRewardedActivityList()
                .stream()
                .anyMatch(rewardActivity1 -> rewardActivity1 == rewardActivity )){
            return Result.valueOf("已领取");
        }

        playerDailyActivity.getRewardedActivityList().add(rewardActivity);
        dailyActivityDAO.updateQueue(playerDailyActivity.convertDailyActivityEntity());

        return Result.success();
    }


}
