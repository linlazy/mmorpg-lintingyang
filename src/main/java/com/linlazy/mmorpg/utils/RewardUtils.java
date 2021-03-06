package com.linlazy.mmorpg.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.linlazy.mmorpg.module.common.reward.Reward;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linlazy
 */
public class RewardUtils {

    public static List<Reward> parseRewards(String rewards){
        List<Reward> rewardList = new ArrayList<>();

        JSONArray jsonArray = JSON.parseArray(rewards);
        for(int i = 0 ; i < jsonArray.size(); i ++){
            JSONArray jsonArray1 = jsonArray.getJSONArray(i);

            Reward reward = new Reward();
            long rewardId = jsonArray1.getLongValue(0);
            int rewardCount = jsonArray1.getIntValue(1);

            reward.setRewardId(rewardId);
            reward.setCount(rewardCount);

            rewardList.add(reward);
        }

        return rewardList;
    }
}
