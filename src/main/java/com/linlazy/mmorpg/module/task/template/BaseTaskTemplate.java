package com.linlazy.mmorpg.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.module.common.event.EventType;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author linlazy
 */
public abstract class BaseTaskTemplate {

    /**
     * 任务模板感兴趣的事件集合
     * @return  返回感兴趣的事件集合
     */
    public  Set<EventType> likeEvent(){
        return Sets.newHashSet();
    }

    private static Map<Integer, BaseTaskTemplate> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(templateId(),this);
    }

    /**
     * 任务模板ID
     * @return  返回任务模板ID
     */
    protected abstract int templateId();

    public static BaseTaskTemplate getTaskTemplate(int templateId){
        return map.get(templateId);
    }

    /**
     * 是否满足前置条件
     * @return
     */
    public boolean isPreCondition(long actorId, JSONObject jsonObject, Task task){
        return true;
    }

    /**
     * 更新任务数据
     * @param actorId
     * @param task
     * @return
     */
    public Task updateTaskData(long actorId, JSONObject jsonObject, Task task){
        return task;
    }

    /**
     * 是否满足达成任务条件
     * @return
     */
    public  boolean isReachCondition(long actorId, Task task){
        return false;
    }


    public abstract String getTaskProcess(Task task);
}
