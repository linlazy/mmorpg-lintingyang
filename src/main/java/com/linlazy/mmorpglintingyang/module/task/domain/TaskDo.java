package com.linlazy.mmorpglintingyang.module.task.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.task.constants.TaskStatus;
import com.linlazy.mmorpglintingyang.module.task.trigger.TaskTrigger;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TaskDo {

    /**
     * 任务唯一标识
     */
    private long taskId;
    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 任务类型
     */
    private int type;

    /**
     * 任务数据
     */
    private JSONObject data;

    /**
     * 任务状态
     */
    private int status;

    /**
     * 任务模板ID
     */
    private int taskTemplateId;

    /**
     * 任务模板参数
     */
    private JSONObject taskTemplateArgs;

    /**
     * 触发类型
     */
    private int triggerType;
    /**
     * 触发参数
     */
    private JSONObject triggerArgs;

    /**
     * 开启时间
     */
    private LocalTime beginTime;
    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 任务是否被开启
     * @return
     */
    public boolean isStart(){
        if(LocalTime.now().isBefore(beginTime)||LocalTime.now().isAfter(endTime)){
            return false;
        }

        if(this.status > TaskStatus.UN_START){
            return true;
        }
        TaskTrigger taskTrigger = TaskTrigger.getTaskTrigger(triggerType);
        return taskTrigger.isTrigger(this);

    }

}
