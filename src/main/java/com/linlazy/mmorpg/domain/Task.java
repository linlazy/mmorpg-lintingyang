package com.linlazy.mmorpg.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.constants.TaskStatus;
import com.linlazy.mmorpg.entity.TaskEntity;
import com.linlazy.mmorpg.trigger.BaseTaskTrigger;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    private LocalTime beginTime;
    /**
     * 结束时间
     */
    private LocalTime endTime;

    public Task(JSONObject taskConfig, TaskEntity task) {
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
        DateTimeFormatter dateTimeFormatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        logger.debug("taskConfig:{}",taskConfig);
        this.beginTime = LocalTime.parse(taskConfig.getString("beginTime"),dateTimeFormatter);
        this.endTime =  LocalTime.parse(taskConfig.getString("endTime"),dateTimeFormatter);
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
        if(LocalTime.now().isBefore(beginTime)||LocalTime.now().isAfter(endTime)){
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

}
