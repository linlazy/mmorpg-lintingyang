package com.linlazy.mmorpglintingyang.module.copy.service;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.copy.domain.CopyDo;
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
            case SCENE_ENTER://进入场景副本
                int sceneId = (int) actorEvent.getData();
                if(globalConfigService.isCopy(sceneId)){
                    handleEnterScene(actorEvent,sceneId);
                }
                break;
            case ATTACK:
                sceneId = (int) actorEvent.getData();
                if(globalConfigService.isCopy(sceneId)){
                    handleAttack(actorEvent,sceneId);
                }
                break;
            case ATTACKED:
                sceneId = (int) actorEvent.getData();
                if(globalConfigService.isCopy(sceneId)){
                    handleAttacked(actorEvent,sceneId);
                }
                break;
            case QUIT_COPY:
                handleQuitCopy(actorEvent);
                break;
            case COPY_SUCCESS:
                handleCopySuccess(actorEvent);
                break;
            case COPY_FAIL:
                handleCopyFail(actorEvent);
                break;
        }

    }

    private void handleAttacked(ActorEvent actorEvent, int sceneId) {

    }

    private void handleAttack(ActorEvent actorEvent, int sceneId) {

    }

    private void handleCopyFail(ActorEvent actorEvent) {

    }

    private void handleCopySuccess(ActorEvent actorEvent) {
        int copyId = (int) actorEvent.getData();
        copyManager.copySuccess(copyId);
    }

    /**
     * 处理退出副本事件
     * @param actorEvent
     */
    private void handleQuitCopy(ActorEvent actorEvent) {
        int copyId = (int) actorEvent.getData();
        copyManager.quitCopy(copyId);
    }

    /**
     * 处理进入场景事件
     * @param actorEvent
     * @param sceneId
     */
    private void handleEnterScene(ActorEvent actorEvent, int sceneId) {
        CopyDo copyDo = copyManager.initCopy(actorEvent.getActorId(), sceneId);
        copyManager.startQuitCopyScheduled(copyDo);
    }

}
