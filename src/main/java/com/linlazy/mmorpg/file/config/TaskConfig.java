package com.linlazy.mmorpg.file.config;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.common.reward.Reward;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
     * 任务模板ID
     */
    private int taskTemplateId;

    /**
     * 任务模板参数
     */
    private JSONObject taskTemplateArgs;

    /**
     * 任务开启时间
     */
    private LocalDateTime beginTime;

    /**
     * 任务结束时间
     */
    private LocalDateTime endTime;

    /**
     * 奖励
     */
    private List<Reward> rewardList;

    /**
     * 触发类型
     */
    private int triggerType;

    /**
     * 触发参数
     */
    private JSONObject triggerArgs;

}
