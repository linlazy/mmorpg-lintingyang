package com.linlazy.mmorpglintingyang.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class TaskTemplate {

    public abstract Set<EventType> likeEvent();

    private static Map<Integer,TaskTemplate> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(templateId(),this);
    }

    protected abstract int templateId();

    public static TaskTemplate getTaskTemplate(int templateId){
        return map.get(templateId);
    }

    /**
     * 是否满足前置条件
     * @return
     */
    public boolean isPreCondition(long actorId, JSONObject jsonObject, TaskDo taskDo){
        return true;
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

}
