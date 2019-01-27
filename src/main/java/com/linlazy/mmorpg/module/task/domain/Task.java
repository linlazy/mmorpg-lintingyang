package com.linlazy.mmorpg.module.task.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.entity.TaskEntity;
import com.linlazy.mmorpg.file.config.TaskConfig;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.task.trigger.BaseTaskTrigger;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author linlazy
 */
@Data
public class Task {

    private static Logger logger = LoggerFactory.getLogger(Task.class);

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
    private JSONObject data = new JSONObject();

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
    private JSONObject taskTemplateArgs = new JSONObject();

    /**
     * 触发类型
     */
    private int triggerType;
    /**
     * 触发参数
     */
    private JSONObject triggerArgs = new JSONObject();

    /**
     * 开启时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;



    private List<Reward> rewardList;

    public Task(TaskConfig taskConfig, TaskEntity task) {
        this.taskId = taskConfig.getTaskId();
        this.actorId = task.getActorId();
//        this.type = taskConfig.ge("type");
        if(!StringUtils.isEmpty(task.getData())){
            this.data =JSONObject.parseObject(task.getData());
        }
        this.status = task.getStatus();
        this.taskTemplateId = taskConfig.getTaskTemplateId() ;
        this.taskTemplateArgs = taskConfig.getTaskTemplateArgs();
        this.triggerType = taskConfig.getTriggerType();
        this.triggerArgs =taskConfig.getTriggerArgs();
        this.beginTime = taskConfig.getBeginTime();
        this.endTime = taskConfig.getEndTime();
        this.rewardList =taskConfig.getRewardList();

    }


    public TaskEntity convertTask(){
        TaskEntity task = new TaskEntity();

        task.setTaskId(this.taskId);
        task.setStatus(this.status);
        task.setActorId(this.actorId);
        task.setData(JSONObject.toJSONString(this.data));

        return task;
    }

    /**
     * 任务是否被开启
     * @return
     */
    public boolean isStart(){
        if(LocalDateTime.now().isBefore(beginTime)||LocalDateTime.now().isAfter(endTime)){
            return false;
        }

        if(this.status > TaskStatus.UN_START){
            return true;
        }
        BaseTaskTrigger taskTrigger = BaseTaskTrigger.getTaskTrigger(triggerType);
        if(taskTrigger.isTrigger(this)){
            this.status = TaskStatus.START_UNCOMPLETE;

            return true;
        }else {
            return false;
        }
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append(String.format("任务编号【%d】",taskId));

        switch (status){
            case TaskStatus.UN_START:
                stringBuilder.append(String.format("任务状态【未开启】"));
                break;
            case TaskStatus.START_UNCOMPLETE:
                stringBuilder.append(String.format("任务状态【已开启未完成】"));
                break;
            case TaskStatus.COMPLETE_UNREWARD:
                stringBuilder.append(String.format("任务状态【已完成未领奖】"));
                break;
            case TaskStatus.REWARDED:
                stringBuilder.append(String.format("任务状态【已领奖】"));
                break;
             default:
        }
        stringBuilder.append(String.format("任务开启时间【%s】",beginTime.toString()));
        stringBuilder.append(String.format("任务结束时间【%s】",endTime));

        stringBuilder.append(String.format("奖励内容"));
        rewardList.forEach(reward -> {
            stringBuilder.append(String.format(reward.toString()));
        });

        return stringBuilder.toString();
    }
}
