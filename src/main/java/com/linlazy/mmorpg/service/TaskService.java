package com.linlazy.mmorpg.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.constants.TaskStatus;
import com.linlazy.mmorpg.dao.TaskDAO;
import com.linlazy.mmorpg.domain.Task;
import com.linlazy.mmorpg.entity.TaskEntity;
import com.linlazy.mmorpg.file.service.TaskConfigService;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.template.task.BaseTaskTemplate;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 任务系统服务类
 * @author linlazy
 */
@Component
public class TaskService {
    /**
     * 玩家任务缓存
     */
    public static LoadingCache<Long, List<Task>> playerTaskCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .recordStats()
            .build(new CacheLoader<Long,List<Task>>() {
                @Override
                public List<Task> load(Long actorId) {

                    TaskConfigService taskConfigService = SpringContextUtil.getApplicationContext().getBean(TaskConfigService.class);
                    TaskDAO taskDao = SpringContextUtil.getApplicationContext().getBean(TaskDAO.class);

                    Set<JSONObject> allTaskConfig = taskConfigService.getAllTaskConfig();
                    return allTaskConfig.stream()
                            .map(taskConfig-> {
                                TaskEntity task = taskDao.getEntityByPK(actorId,taskConfig.getIntValue("taskId"));
                                if(task == null){
                                    task = new TaskEntity();
                                    task.setTaskId(taskConfig.getIntValue("taskId"));
                                    task.setActorId(actorId);
                                }
                                return new Task(taskConfig,task);
                            }).collect(Collectors.toList());
                }
            });


    @Autowired
    private TaskConfigService taskConfigService;
    @Autowired
    TaskDAO taskDao;
    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void listenEvent(ActorEvent<JSONObject> actorEvent){
        List<Task> allTask =getActorAllTaskDo(actorEvent.getActorId());
        allTask.stream()
            .filter(taskDo -> BaseTaskTemplate.getTaskTemplate(taskDo.getTaskTemplateId()).likeEvent().contains(actorEvent.getEventType()))
            .forEach(taskDo -> doComplete(actorEvent.getActorId(),actorEvent.getData(),taskDo));
    }

    private void doComplete(long actorId, JSONObject jsonObject, Task task) {
        if(!doPreCondition(actorId,jsonObject, task)){
            //存档
            taskDao.updateQueue(task.convertTask());
            return;
        }
        BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(task.getTaskTemplateId());
        if(taskTemplate.isReachCondition(actorId, task)){
            task.setStatus(TaskStatus.COMPLETE_UNREWARD);
            //存档
            taskDao.updateQueue(task.convertTask());
            return;
        }
        taskTemplate.updateTaskData(actorId,jsonObject, task);
        if(taskTemplate.isReachCondition(actorId, task)){
            task.setStatus(TaskStatus.COMPLETE_UNREWARD);
        }
        //存档
        taskDao.updateQueue(task.convertTask());
    }

    private boolean doPreCondition(long actorId, JSONObject jsonObject, Task task) {
        if(!task.isStart()){
            return false;
        }
        if (task.getStatus() != TaskStatus.START_UNCOMPLETE){
            return false;
        }
        BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(task.getTaskTemplateId());
        return taskTemplate.isPreCondition(actorId, jsonObject, task);
    }


    public List<Task> getActorAllTaskDo(long actorId){
        try {
            return playerTaskCache.get(actorId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
