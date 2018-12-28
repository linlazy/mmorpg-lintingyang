package com.linlazy.mmorpglintingyang.module.task.service;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.task.config.TaskConfigService;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;
import com.linlazy.mmorpglintingyang.module.task.template.TaskTemplate;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 任务系统服务类
 */
@Component
public class TaskService {


    private static Map<EventType,Set<TaskTemplate>> map = new HashMap<>();

    private TaskConfigService taskConfigService;

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void listenEvent(ActorEvent actorEvent){
        switch (actorEvent.getEventType()){
            case TASK_TRIGGER:
                handlerTaskTrigger(actorEvent);
        }
    }

    private void handlerTaskTrigger(ActorEvent actorEvent) {
        //获取所有关心任务触发启动的任务
        SpringContextUtil.getApplicationContext().getBeansOfType(TaskTemplate.class).values().stream()
            .forEach(taskTemplate -> {
                Set<EventType> eventTypes = taskTemplate.likeEvent();
                eventTypes.stream()
                    .forEach(eventType -> {
                        map.computeIfAbsent(eventType, k -> new HashSet<>());
                        map.get(eventType).add(taskTemplate);
                    });
            });

        Set<TaskDo> taskDoSet = taskConfigService.getAllTaskDo();
    }


}
