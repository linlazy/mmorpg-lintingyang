package com.linlazy.mmorpglintingyang.module.copy.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.copy.manager.CopyManager;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CopyService {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private CopyManager copyManager;


    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }


    @Subscribe
    public void listenEvent(ActorEvent actorEvent){

        switch (actorEvent.getEventType()){
            case COPY_ACTOR_DEAD:
                //副本玩家死亡
                handleCopyActorDead(actorEvent);
                break;
            case COPY_BOSS_DEAD:
                //副本BOSS死亡
                handleCopyBOSSDead(actorEvent);
                break;
            case QUIT_COPY:
                //退出副本
                handleQuitCopy(actorEvent);
                break;
            case COPY_SUCCESS:
                //挑战成功
                handleCopySuccess(actorEvent);
                break;
            case COPY_FAIL:
                //挑战失败
                handleCopyFail(actorEvent);
                break;
            default:
                break;
        }

    }

    private void handleCopyBOSSDead(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject)actorEvent.getData();
        int copyId = jsonObject.getIntValue("copyId");
        copyManager.handleCopyBOSSDead(copyId);
    }

    private void handleCopyFail(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject)actorEvent.getData();
        int copyId = jsonObject.getIntValue("copyId");
        copyManager.copyFail(copyId);
    }

    private void handleCopySuccess(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject)actorEvent.getData();
        int copyId = jsonObject.getIntValue("copyId");
        copyManager.copySuccess(copyId);
    }

    /**
     * 处理退出副本事件
     * @param actorEvent
     */
    private void handleQuitCopy(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject)actorEvent.getData();
        int copyId = jsonObject.getIntValue("copyId");
        copyManager.quitCopy(copyId);
    }



    /**
     * 处理副本玩家死亡事件
     * @param actorEvent
     */
    private void handleCopyActorDead(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject)actorEvent.getData();
        int copyId = jsonObject.getIntValue("copyId");
        copyManager.handleCopyActorDead(copyId);
    }

}
