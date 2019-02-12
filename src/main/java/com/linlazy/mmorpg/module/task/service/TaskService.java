package com.linlazy.mmorpg.module.task.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.dao.TaskDAO;
import com.linlazy.mmorpg.entity.TaskEntity;
import com.linlazy.mmorpg.file.config.TaskConfig;
import com.linlazy.mmorpg.file.service.TaskConfigService;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.module.player.domain.PlayerTask;
import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.module.task.reward.BaseTaskReward;
import com.linlazy.mmorpg.module.task.template.BaseTaskTemplate;
import com.linlazy.mmorpg.server.common.Result;
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

    @Autowired
    private TaskDAO taskDAO;
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
                    .forEach(task ->{

                        if(task.getStatus() ==TaskStatus.ACCEPT_UN_COMPLETE){
                            BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(task.getTaskTemplateId());
                            if(taskTemplate.likeEvent().contains(actorEvent.getEventType())){

                                if( taskTemplate.isPreCondition(actorEvent.getActorId(),actorEvent.getData(),task)){
                                    taskTemplate.updateTaskData(actorEvent.getActorId(),actorEvent.getData(),task);

                                    if(taskTemplate.isReachCondition(actorEvent.getActorId(),task)){
                                        task.setStatus(TaskStatus.ACCEPT_ABLE_COMPLETE);
                                        taskDao.updateQueue(task.convertTaskEntity());
                                    }
                                }
                            }
                        }else if(task.getStatus() ==TaskStatus.ACCEPT_ABLE_COMPLETE){
                            if(actorEvent.getEventType().equals(EventType.ACTOR_ITEM_CHANGE)){
                                BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(task.getTaskTemplateId());
                                taskTemplate.updateTaskData(actorEvent.getActorId(),actorEvent.getData(),task);
                                if(!taskTemplate.isReachCondition(actorEvent.getActorId(),task)){
                                    task.setStatus(TaskStatus.ACCEPT_UN_COMPLETE);
                                    taskDao.updateQueue(task.convertTaskEntity());
                                }
                            }
                        }
            });
    }


    @Subscribe
    public void loginEvent(ActorEvent actorEvent){
        if(actorEvent.getEventType().equals(EventType.LOGIN) || actorEvent.getEventType().equals(EventType.REGISTER)){
            PlayerTask playerTask = getPlayerTask(actorEvent.getActorId());
            playerTask.getMap().values().stream()
                .forEach(task ->{
                    if(task.getStatus() ==TaskStatus.UN_START){
                        task.doStart();
                    }
                });
        }
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
     * 提交任务
     * @param actorId
     * @param taskId
     * @return
     */
    public Result<?> commitTask(long actorId, long taskId){

        PlayerTask playerTask = getPlayerTask(actorId);
        Task task = playerTask.getMap().get(taskId);
        if(task == null){
            return Result.valueOf("参数有误");
        }

        if(task.getStatus() != TaskStatus.ACCEPT_ABLE_COMPLETE){
            return  Result.valueOf("参数有误");
        }

        boolean completed = task.doComplete();
        if(completed){
            int taskRewardType = task.getTaskReward().getIntValue("taskRewardType");
            BaseTaskReward baseTaskReward = BaseTaskReward.getBaseTaskReward(taskRewardType);
            Result<?> result = baseTaskReward.doTaskReward(actorId, task);
        }
        return Result.success();
    }

    /**
     * 开启任务
     * @param actorId
     * @param taskId
     * @return
     */
    public Result<?> startTask(long actorId, long taskId){
        PlayerTask playerTask = getPlayerTask(actorId);
        Task task = playerTask.getMap().get(taskId);
        if(task == null){
            return Result.valueOf("参数有误");
        }
        if(task.getStatus() != TaskStatus.UN_START){
            return Result.valueOf("参数有误");
        }

        boolean started = task.doStart();
        if(!started){
            return Result.valueOf("开启任务失败");
        }

        return Result.success(task.toString());
    }
    /**
     * 接受任务
     * @param actorId
     * @param taskId
     * @return
     */
    public Result<?> acceptTask(long actorId, long taskId){

        PlayerTask playerTask = getPlayerTask(actorId);
        Task task = playerTask.getMap().get(taskId);
        if(task == null){
            return Result.valueOf("参数有误");
        }
        if(task.getStatus() != TaskStatus.START_UN_ACCEPT){
            return Result.valueOf("参数有误");
        }
        boolean accepted = task.doAccept();
        if(!accepted){
            return Result.valueOf("接受任务失败");
        }
       task.doAbleComplete();

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
