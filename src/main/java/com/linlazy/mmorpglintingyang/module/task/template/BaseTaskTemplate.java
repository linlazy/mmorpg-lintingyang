package com.linlazy.mmorpglintingyang.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;

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
    public abstract Set<EventType> likeEvent();

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
