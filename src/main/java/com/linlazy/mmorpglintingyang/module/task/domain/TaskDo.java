package com.linlazy.mmorpglintingyang.module.task.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.task.constants.TaskStatus;
import com.linlazy.mmorpglintingyang.module.task.entity.Task;
import com.linlazy.mmorpglintingyang.module.task.trigger.TaskTrigger;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    private LocalTime beginTime;
    /**
     * 结束时间
     */
    private LocalTime endTime;

    public TaskDo(JSONObject taskConfig, Task task) {
        this.taskId = taskConfig.getIntValue("taskId");
        this.actorId = task.getActorId();
        this.type = taskConfig.getIntValue("type");

        if(!StringUtils.isEmpty(task.getData())){
            this.data =JSONObject.parseObject(task.getData());
        }

        this.status = task.getStatus();
        this.taskTemplateId = taskConfig.getIntValue("taskTemplateId") ;

        String taskTemplateArgs = taskConfig.getString("taskTemplateArgs");
        if(!StringUtils.isEmpty(taskTemplateArgs)){
            this.taskTemplateArgs = JSONObject.parseObject(taskTemplateArgs);
        }

        this.triggerType = taskConfig.getIntValue("triggerType") ;

        String triggerArgs = taskConfig.getString("triggerArgs");
        if(!StringUtils.isEmpty(triggerArgs)){
            this.triggerArgs = JSONObject.parseObject(triggerArgs);
        }
        DateTimeFormatter dateTimeFormatter =DateTimeFormatter.ofPattern("2018-12-30");
        this.beginTime = LocalTime.parse(taskConfig.getString("beginTime"),dateTimeFormatter);
        this.endTime =  LocalTime.parse(taskConfig.getString("endTime"),dateTimeFormatter);
    }

    public Task convertTask(){
        Task task = new Task();

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
        if(LocalTime.now().isBefore(beginTime)||LocalTime.now().isAfter(endTime)){
            return false;
        }

        if(this.status > TaskStatus.UN_START){
            return true;
        }
        TaskTrigger taskTrigger = TaskTrigger.getTaskTrigger(triggerType);
        if(taskTrigger.isTrigger(this)){
            this.status = TaskStatus.START_UNCOMPLETE;
            return true;
        }else {
            return false;
        }
    }

}