package com.linlazy.mmorpglintingyang.module.task.type;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 等级升到N级
 */
@Component
public class Task1 {

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void listenEvent(ActorEvent actorEvent){
        switch (actorEvent.getEventType()){
            //玩家等级提升事件
            case ACTOR_LEVEL_UP:
                //任务触发事件
            case TASK_TRIGGER:
                handle(actorEvent);
        }
    }


    //任务开启事件 -->检查任务完成情况
    //任务内容相关事件-->满足任务前置条件 -->更新任务数据 -->检查完成情况
    //任务奖励领取

    // 任务状态
    // 1.未开启
    // 2.已开启，未完成
    // 1.已完成，可领取
    // 2.已领取


    private void handle(ActorEvent actorEvent) {

        //任务内容相关事件

        //是否满足前置条件
        if(isReachCondition(actorEvent.getActorId(), (JSONObject) actorEvent.getData())){
            //更新任务数据
            updateTaskData();
            //是否任务完成
            if(isCompeled(actorEvent.getActorId(),(JSONObject) actorEvent.getData())){
                updateTaskStatus(actorEvent.getActorId(),(JSONObject) actorEvent.getData());
            }
            //存档DB
        }

        //任务开启事件

        // 任务是否开启
        if(isStart()){
            //是否任务完成
            if(isCompeled(actorEvent.getActorId(),(JSONObject) actorEvent.getData())){
                updateTaskStatus(actorEvent.getActorId(),(JSONObject) actorEvent.getData());
            }
            //存档
        }

    }

    private boolean isStart() {
        return false;
    }

    private void updateTaskStatus(long actorId, JSONObject data) {

    }

    /**
     * 是否满足前置条件
     * @param actorId
     * @param data
     * @return
     */
    private boolean isReachCondition(long actorId, JSONObject data) {
        return false;
    }

    private void updateTaskData() {

    }



    @Autowired
    private UserManager userManager;

    public boolean isCompeled(long actorId, JSONObject taskConfig){
        int needLevel = taskConfig.getIntValue("level");
        return userManager.getUser(actorId).getLevel() >= needLevel;
    }
}
