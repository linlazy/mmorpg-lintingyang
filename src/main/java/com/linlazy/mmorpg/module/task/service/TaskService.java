package com.linlazy.mmorpg.module.task.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.dao.TaskDAO;
import com.linlazy.mmorpg.module.player.domain.PlayerTask;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.entity.TaskEntity;
import com.linlazy.mmorpg.file.config.TaskConfig;
import com.linlazy.mmorpg.file.service.TaskConfigService;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.module.task.template.BaseTaskTemplate;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * 任务系统服务类
 * @author linlazy
 */
@Component
public class TaskService {

    @Autowired
    private RewardService rewardService;


    /**
     * 玩家任务缓存
     */
    public static LoadingCache<Long, PlayerTask> playerTaskCache = CacheBuilder.newBuilder()
            .recordStats()
            .build(new CacheLoader<Long,PlayerTask>() {
                @Override
                public PlayerTask load(Long actorId) {

                    PlayerTask playerTask = new PlayerTask();

                    TaskConfigService taskConfigService = SpringContextUtil.getApplicationContext().getBean(TaskConfigService.class);
                    TaskDAO taskDao = SpringContextUtil.getApplicationContext().getBean(TaskDAO.class);

                    Collection<TaskConfig> allTaskConfig = taskConfigService.getAllTaskConfig();
                     allTaskConfig.stream()
                            .forEach(taskConfig-> {
                                TaskEntity taskEntity = taskDao.getEntityByPK(taskConfig.getTaskId(),actorId);
                                if(taskEntity == null){
                                    taskEntity = new TaskEntity();
                                    taskEntity.setTaskId(taskConfig.getTaskId());
                                    taskEntity.setActorId(actorId);
                                }
                                Task task = new Task(taskConfig, taskEntity);
                                playerTask.getMap().put(task.getTaskId(),task);
                            });
                    return playerTask;
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
        PlayerTask playerTask = getPlayerTask(actorEvent.getActorId());
        playerTask.getMap().values().stream()
            .filter(taskDo -> BaseTaskTemplate.getTaskTemplate(taskDo.getTaskTemplateId()).likeEvent().contains(actorEvent.getEventType()))
            .forEach(taskDo -> doComplete(actorEvent.getActorId(),actorEvent.getData(),taskDo));
    }

    private void doComplete(long actorId, JSONObject jsonObject, Task task) {
        if(!doPreCondition(actorId,jsonObject, task)){
            return;
        }
        BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(task.getTaskTemplateId());
        if(taskTemplate.isReachCondition(actorId, task) && task.getStatus() == TaskStatus.START_UNCOMPLETE){
            task.setStatus(TaskStatus.COMPLETE_UNREWARD);
            //存档
            taskDao.updateQueue(task.convertTask());
            return;
        }
        taskTemplate.updateTaskData(actorId,jsonObject, task);
        if(taskTemplate.isReachCondition(actorId, task) && task.getStatus() == TaskStatus.START_UNCOMPLETE){
            task.setStatus(TaskStatus.COMPLETE_UNREWARD);
        }
        //存档
        taskDao.updateQueue(task.convertTask());
    }

    private boolean doPreCondition(long actorId, JSONObject jsonObject, Task task) {
        if(!task.isStart()){
            return false;
        }
        if (task.getStatus() < TaskStatus.START_UNCOMPLETE){
            return false;
        }
        BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(task.getTaskTemplateId());
        return taskTemplate.isPreCondition(actorId, jsonObject, task);
    }


    public PlayerTask getPlayerTask(long actorId){
        try {
            return playerTaskCache.get(actorId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 领取任务奖励
     * @param actorId
     * @param taskId
     * @return
     */
    public Result<?> rewardTask(long actorId, long taskId){

        PlayerTask playerTask = getPlayerTask(actorId);
        Task task = playerTask.getMap().get(taskId);
        if(task == null){
            return Result.valueOf("参数有误");
        }

        if(task.getStatus() != TaskStatus.COMPLETE_UNREWARD){
            return  Result.valueOf("任务状态不对");
        }

        task.setStatus(TaskStatus.REWARDED);
        rewardService.addRewardList(actorId,task.getRewardList());


        return Result.success();
    }


    /**
     * 查看玩家任务信息
     * @param actorId
     * @return
     */
    public Result<?> taskInfo(long actorId){
        Collection<TaskConfig> allTaskConfig = taskConfigService.getAllTaskConfig();
        PlayerTask playerTask = getPlayerTask(actorId);

        allTaskConfig.stream().forEach(taskConfig -> {
            Task task = playerTask.getMap().get(taskConfig.getTaskId());
            if(task == null){

                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setActorId(actorId);
                task = new Task(taskConfig,taskEntity);
                playerTask.getMap().put(task.getTaskId(),task);
            }
        });

        return Result.success(playerTask.toString());
    }
}
