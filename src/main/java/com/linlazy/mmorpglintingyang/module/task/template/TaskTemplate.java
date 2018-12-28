package com.linlazy.mmorpglintingyang.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.task.constants.TaskStatus;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;

import java.util.Set;

public abstract class TaskTemplate {

    public abstract Set<EventType> likeEvent();

    /**
     * 是否满足前置条件
     * @return
     */
    public boolean isPreCondition(long actorId, JSONObject jsonObject, TaskDo taskDo){
        return true;
    }

    /**
     * 执行前置条件
     * @param actorId
     * @param jsonObject
     * @param taskDo
     */
    public boolean doPreCondition(long actorId,JSONObject jsonObject, TaskDo taskDo){
        if(!taskDo.isStart()){
            return false;
        }

        return taskDo.getStatus() == TaskStatus.START_UNCOMPLETE;
    }

    /**
     * 更新任务数据
     * @param actorId
     * @param taskDo
     * @return
     */
    public  TaskDo updateTaskData(long actorId,JSONObject jsonObject,TaskDo taskDo){
        return taskDo;
    }

    /**
     * 是否满足达成任务条件
     * @return
     */
    public  boolean isReachCondition(long actorId, TaskDo taskDo){
        return false;
    }


    public void doComplete(long actorId,TaskDo taskDo){
        if(isReachCondition(actorId,taskDo)){
            taskDo.setStatus(TaskStatus.COMPLETE_UNREWARD);
        }
    }

}
