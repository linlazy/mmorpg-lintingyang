package com.linlazy.mmorpg.module.task.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.dao.TaskDAO;
import com.linlazy.mmorpg.entity.TaskEntity;
import com.linlazy.mmorpg.file.config.TaskConfig;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.module.task.trigger.BaseTaskTrigger;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

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
     * 触发条件
     */
    private Map<Integer,TriggerCondition> triggerConditionMap = new HashMap<>();



    /**
     * 奖励
     */
    private List<Reward> rewardList;

    public Task(TaskConfig taskConfig, TaskEntity task) {
        this.taskId = taskConfig.getTaskId();
        this.actorId = task.getActorId();
        if(!StringUtils.isEmpty(task.getData())){
            this.data =JSONObject.parseObject(task.getData());
        }
        this.status = task.getStatus();
        this.taskTemplateId = taskConfig.getTaskTemplateId() ;
        this.taskTemplateArgs = taskConfig.getTaskTemplateArgs();
        this.rewardList =taskConfig.getRewardList();
        this.triggerConditionMap = taskConfig.getTriggerConditionMap();
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
     * 任务是否开启
     * @return
     */
    public boolean isStart(){
        if(this.status > TaskStatus.UN_START){
            return true;
        }

        boolean isStart = triggerConditionMap.values().stream()
                .allMatch(triggerCondition -> {
                    BaseTaskTrigger taskTrigger = BaseTaskTrigger.getTaskTrigger(triggerCondition.getTriggerType());
                    return taskTrigger.isTrigger(this);
                });

        if(isStart){
            if (isAutoAcceptWithStart()){
                this.status = TaskStatus.ACCEPT_UN_COMPLETE;
            }else {
                this.status = TaskStatus.ACCEPT_UN_COMPLETE;
            }
            TaskDAO taskDAO = SpringContextUtil.getApplicationContext().getBean(TaskDAO.class);
            taskDAO.insertQueue(this.convertTaskEntity());
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
            case TaskStatus.ACCEPT_UN_COMPLETE:
                stringBuilder.append(String.format("任务状态【已开启未完成】"));
                break;
            case TaskStatus.COMPLETE_UN_REWARD:
                stringBuilder.append(String.format("任务状态【已完成未领奖】"));
                break;
            case TaskStatus.REWARDED:
                stringBuilder.append(String.format("任务状态【已领奖】"));
                break;
             default:
        }

        stringBuilder.append(String.format("奖励内容"));
        rewardList.forEach(reward -> {
            stringBuilder.append(String.format(reward.toString()));
        });

        return stringBuilder.toString();
    }
}
