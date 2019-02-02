package com.linlazy.mmorpg.file.config;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.task.domain.TriggerCondition;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务配置类
 * @author linlazy
 */
@Data
public class TaskConfig {

    /**
     * 任务ID
     */
    private long taskId;

    /**
     * 描述
     */
    private String desc;

    /**
     * 任务模板ID
     */
    private int taskTemplateId;

    /**
     * 任务模板参数
     */
    private JSONObject taskTemplateArgs;

    /**
     * 奖励
     */
    private List<Reward> rewardList;

    /**
     * 触发条件
     */
    private Map<Integer, TriggerCondition> triggerConditionMap = new HashMap<>();

    /**
     * 开启任务时自动接受任务
     */
    private boolean autoAcceptWithStart;

    /**
     * 达到条件时任务时自动完成
     */
    private boolean autoCompleteWithReachCondition;

}
