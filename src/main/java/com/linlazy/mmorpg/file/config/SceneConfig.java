package com.linlazy.mmorpg.file.config;

import com.alibaba.fastjson.JSONArray;
import com.linlazy.mmorpg.module.common.reward.Reward;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linlazy
 */
@Data
public class SceneConfig {

    /**
     * 场景ID
     */
    private int sceneId;

    /**
     * 场景名称
     */
    private String name;

    /**
     * 超时时间
     */
    private int overTimeSeconds;

    /**
     * 通过奖励
     */
    private List<Reward> rewardList  = new ArrayList<>();

    /**
     * 相邻场景
     */
    private List<Integer> neighborSet = new ArrayList<>();

    /**
     * 场景实体
     */
    private JSONArray sceneEntities = new JSONArray();


}
