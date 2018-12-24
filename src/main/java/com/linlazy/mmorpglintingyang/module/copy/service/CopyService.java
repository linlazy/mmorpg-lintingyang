package com.linlazy.mmorpglintingyang.module.copy.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.copy.manager.CopyManager;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.team.manager.TeamManager;
import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CopyService {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private SceneConfigService sceneConfigService;

    @Autowired
    private CopyManager copyManager;
    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private UserDao userDao;

    @Autowired
    private TeamManager teamManager;

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    //当进入的场景为副本时触发初始化
    @Subscribe
    public void listenEvent(ActorEvent actorEvent){
        if(actorEvent.getEventType().equals(EventType.SCENE_ENTER)){

            int sceneId = (int) actorEvent.getData();
            if(globalConfigService.isCopy(sceneId)){

                 int copyId = copyManager.initCopy(actorEvent.getActorId());

                JSONObject copyConfig = sceneConfigService.getCopyConfig(sceneId);

                int times = copyConfig.getIntValue("times");
                //启动定时退出副本调度
                startCopy(actorEvent.getActorId(),times);
            }
        }

        //退出副本逻辑
        if(actorEvent.getEventType().equals(EventType.QUIT_COPY)){
            int sceneId = (int) actorEvent.getData();
            JSONObject copyConfig = sceneConfigService.getCopyConfig(sceneId);
            int targetSceneId = copyConfig.getJSONArray("sceneIds").getIntValue(0);
            sceneManager.moveToScene(actorEvent.getActorId(),targetSceneId);
            int copyId = 0;
            copyManager.quitCopy(copyId);
        }


    }


    /**
     *
     * @param times
     */
    private void startCopy(long actorId, int times) {

        //到达时间后挑战结束
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay((Runnable) () -> {

            EventBusHolder.post(new ActorEvent<>(actorId,EventType.QUIT_COPY,userDao.getUser(actorId).getSceneId()));

        },0L,times , TimeUnit.SECONDS);
    }
}
