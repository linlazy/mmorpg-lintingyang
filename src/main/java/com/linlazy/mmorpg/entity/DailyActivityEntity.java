package com.linlazy.mmorpg.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 活跃度实体类
 * @author linlazy
 */
@Data
@Table("daily_activity")
public class DailyActivityEntity extends Entity {

    /**
     * 玩家ID
     */
    @Cloumn(pk = true)
    private long actorId;

    /**
     * 当前活跃度
     */
    @Cloumn
    private int currentActivity;

    /**
     * 已领过活跃度
     */
    @Cloumn
    private String rewardedActivities;

    private List<Integer> rewardedActivityList;

    /**
     * 下一次重置活跃度时间
     */
    @Cloumn
    private long nextResetTime;

    @Override
    public void beforeWriteDB() {
        rewardedActivities = JSONObject.toJSONString(rewardedActivityList);
    }

    @Override
    public void afterReadDB() {
        if(!StringUtils.isEmpty(rewardedActivities)){
            rewardedActivityList = JSONObject.parseObject(rewardedActivities, new TypeReference<ArrayList>(){});
        }
    }
}
