package com.linlazy.mmorpg.module.task.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.module.task.domain.TaskDo;
import com.linlazy.mmorpg.module.task.manager.TaskManager;
import com.linlazy.mmorpg.module.task.template.BaseTaskTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 任务系统服务类
 * @author linlazy
 */
@Component
public class TaskService {


    @Autowired
    private TaskManager taskManager;

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void listenEvent(ActorEvent<JSONObject> actorEvent){
        Set<TaskDo> allTaskDo = taskManager.getActorAllTaskDo(actorEvent.getActorId());
        allTaskDo.stream()
            .filter(taskDo -> BaseTaskTemplate.getTaskTemplate(taskDo.getTaskTemplateId()).likeEvent().contains(actorEvent.getEventType()))
            .forEach(taskDo -> doComplete(actorEvent.getActorId(),actorEvent.getData(),taskDo));
    }

    private void doComplete(long actorId, JSONObject jsonObject, TaskDo taskDo) {
        if(!doPreCondition(actorId,jsonObject,taskDo)){
            //存档
            taskManager.update(taskDo.convertTask());
            return;
        }
        BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(taskDo.getTaskTemplateId());
        if(taskTemplate.isReachCondition(actorId,taskDo)){
            taskDo.setStatus(TaskStatus.COMPLETE_UNREWARD);
            //存档
            taskManager.update(taskDo.convertTask());
            return;
        }
        taskTemplate.updateTaskData(actorId,jsonObject,taskDo);
        if(taskTemplate.isReachCondition(actorId,taskDo)){
            taskDo.setStatus(TaskStatus.COMPLETE_UNREWARD);
        }
        //存档
        taskManager.update(taskDo.convertTask());
    }

    private boolean doPreCondition(long actorId, JSONObject jsonObject, TaskDo taskDo) {
        if(!taskDo.isStart()){
            return false;
        }
        if (taskDo.getStatus() != TaskStatus.START_UNCOMPLETE){
            return false;
        }
        BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(taskDo.getTaskTemplateId());
        return taskTemplate.isPreCondition(actorId, jsonObject, taskDo);
    }

}
