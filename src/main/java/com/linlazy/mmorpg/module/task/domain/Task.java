package com.linlazy.mmorpg.module.task.domain;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.dao.TaskDAO;
import com.linlazy.mmorpg.entity.TaskEntity;
import com.linlazy.mmorpg.file.config.TaskConfig;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.task.condition.accept.AcceptCondition;
import com.linlazy.mmorpg.module.task.condition.start.StartCondition;
import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.module.task.template.BaseTaskTemplate;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 任务名称
     */
    private String taskName;
    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 任务类型
     */
    private int type;

    /**
     * 描述
     */
    private String desc;

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
    private JSONObject taskTemplateArgs;


    /**
     * 开启任务时自动接受任务
     */
    private boolean autoAcceptWithStart;

    /**
     * 达到条件时任务时自动完成
     */
    private boolean autoCompleteWithReachCondition;

    /**
     * 开启条件
     */
    private Map<Integer,TriggerCondition> startConditionMap = new HashMap<>();

    /**
     * 接受条件
     */
    private Map<Integer,TriggerCondition> acceptConditionMap = new HashMap<>();

    /**
     * 任务奖励
     */
    private JSONObject taskReward = new JSONObject();

    /**
     * 奖励
     */
    private List<Reward> rewardList;

    public Task(TaskConfig taskConfig, TaskEntity task) {
        this.taskId = taskConfig.getTaskId();
        this.taskName = taskConfig.getTaskName();
        this.actorId = task.getActorId();
        if(!StringUtils.isEmpty(task.getData())){
            this.data =JSONObject.parseObject(task.getData());
        }
        this.status = task.getStatus();

        this.desc = taskConfig.getDesc();
        this.taskTemplateId = taskConfig.getTaskTemplateId() ;
        this.taskTemplateArgs = taskConfig.getTaskTemplateArgs();
        this.rewardList =taskConfig.getRewardList();
        this.startConditionMap = taskConfig.getTriggerConditionMap();
        this.autoAcceptWithStart = taskConfig.isAutoAcceptWithStart();
        this.autoCompleteWithReachCondition = taskConfig.isAutoCompleteWithReachCondition();
    }


    public TaskEntity convertTaskEntity(){
        TaskEntity task = new TaskEntity();

        task.setTaskId(this.taskId);
        task.setStatus(this.status);
        task.setActorId(this.actorId);
        task.setData(JSONObject.toJSONString(this.data));

        return task;
    }

    /**
     * 任务开启
     * @return
     */
    public boolean doStart(){
        if(this.status >= TaskStatus.START_UN_ACCEPT){
            return true;
        }

        boolean isStart = startConditionMap.values().stream()
                .allMatch(triggerCondition -> {
                    StartCondition startCondition = StartCondition.getStartCondition(triggerCondition.getTriggerType());
                    return startCondition.isReachCondition(actorId,this);
                });

        if(isStart){
            this.status = TaskStatus.START_UN_ACCEPT;
            TaskDAO taskDAO = SpringContextUtil.getApplicationContext().getBean(TaskDAO.class);
            taskDAO.insertQueue(this.convertTaskEntity());
            return true;
        }else {
            return false;
        }
    }

    /**
     * 任务接受
     * @return
     */
    public boolean doAccept(){
        if(this.status >= TaskStatus.ACCEPT_UN_COMPLETE){
            return false;
        }

        boolean isAccept = acceptConditionMap.values().stream()
                .allMatch(triggerCondition -> {
                    AcceptCondition acceptCondition = AcceptCondition.getAcceptCondition(triggerCondition.getTriggerType());
                    return acceptCondition.isReachCondition(actorId,this);
                });

        if(isAccept){
            this.status = TaskStatus.ACCEPT_UN_COMPLETE;
            TaskDAO taskDAO = SpringContextUtil.getApplicationContext().getBean(TaskDAO.class);
            taskDAO.updateQueue(this.convertTaskEntity());
            return true;
        }else {
            return false;
        }
    }
    /**
     * 任务可完成
     * @return
     */
    public boolean doAbleComplete(){
        if(this.status >= TaskStatus.ACCEPT_ABLE_COMPLETE){
            return false;
        }

        BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(taskTemplateId);
        boolean reachCondition = taskTemplate.isReachCondition(actorId, this);
        if(reachCondition){
            this.status = TaskStatus.ACCEPT_ABLE_COMPLETE;
            TaskDAO taskDAO = SpringContextUtil.getApplicationContext().getBean(TaskDAO.class);
            taskDAO.updateQueue(this.convertTaskEntity());
        }
        return reachCondition;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append(String.format("任务编号【%d】",taskId));

        switch (status){
            case TaskStatus.UN_START:
                stringBuilder.append(String.format("任务状态【未开启】"));
                break;
            case TaskStatus.START_UN_ACCEPT:
                stringBuilder.append(String.format("任务状态【已开启未接受】"));
                break;
            case TaskStatus.ACCEPT_UN_COMPLETE:
                stringBuilder.append(String.format("任务状态【已接受未完成】"));
                break;
            case TaskStatus.ACCEPT_ABLE_COMPLETE:
                stringBuilder.append(String.format("任务状态【已接受可完成】"));
                break;
            case TaskStatus.COMPLETED:
                stringBuilder.append(String.format("任务状态【已完成】"));
                break;
             default:
        }

        stringBuilder.append(String.format("任务描述【%s】",desc));


        for(TriggerCondition startCondition: startConditionMap.values()){
        }

        stringBuilder.append(String.format("开启条件【%s】",null));
        stringBuilder.append(String.format("完成条件【%s】",null));


        stringBuilder.append(String.format("奖励内容"));
        rewardList.forEach(reward -> {
            stringBuilder.append(String.format(reward.toString()));
        });

        return stringBuilder.toString();
    }

    public boolean doComplete() {
        if(this.status != TaskStatus.ACCEPT_ABLE_COMPLETE){
            return false;
        }
        this.status = TaskStatus.COMPLETED;
        TaskDAO taskDAO = SpringContextUtil.getApplicationContext().getBean(TaskDAO.class);
        taskDAO.updateQueue(this.convertTaskEntity());


        BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(taskTemplateId);
        if(taskTemplate.likeEvent().contains(EventType.ACTOR_ITEM_CHANGE)){
            PlayerBackpackService playerBackpackService = SpringContextUtil.getApplicationContext().getBean(PlayerBackpackService.class);
            List<Item> items = new ArrayList<>();
            playerBackpackService.pop(actorId, Lists.newArrayList());
            for(Item item: items){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("itemId",item.getItemId());
                EventBusHolder.post(new ActorEvent<>(actorId,EventType.ACTOR_ITEM_CHANGE,jsonObject));
            }
        }
        return true;
    }
}
