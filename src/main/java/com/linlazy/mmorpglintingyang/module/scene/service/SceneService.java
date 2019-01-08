package com.linlazy.mmorpglintingyang.module.scene.service;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.scene.manager.NpcManager;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.scene.push.ScenePushHelper;
import com.linlazy.mmorpglintingyang.module.scene.validator.SceneValidator;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 玩家场景服务类
 * @author linlazy
 */
@Component
public class SceneService {

    @Autowired
    private SceneManager sceneManager;

    @Autowired
    private SceneValidator sceneValidator;

    @Autowired
    private NpcManager npcManager;
    @Autowired
    private UserManager userManager;


    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void listenEvent(ActorEvent actorEvent){
        switch (actorEvent.getEventType()){
            case SCENE_MONSTER_DEAD:
                handlerSceneMonsterDead(actorEvent);
                break;
            default:
                break;
        }
    }

    /**
     * 处理场景怪物死亡事件
     * @param actorEvent
     */
    private void handlerSceneMonsterDead(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject) actorEvent.getData();
        long actorId = jsonObject.getLongValue("actorId");
        Set<Long> onlineActorIds = SessionManager.getOnlineActorIds();
        onlineActorIds.stream()
                .filter(onlineActorId -> onlineActorId !=actorId)
                .forEach(onlineActorId ->{
                    int sceneId = jsonObject.getIntValue("sceneId");
                    if(userManager.getUser(onlineActorId).getSceneId() == sceneId){
                        SceneEntityDo monsterDo = jsonObject.getObject("monsterDo", SceneEntityDo.class);
                        ScenePushHelper.pushMonster(onlineActorId, Lists.newArrayList(monsterDo));
                    }
                });

    }


    /**
     * 移动到某个场景
     * @param targetSceneId
     */
    public Result<?> moveTo(long actorId,int targetSceneId){

        // 1 target场景是否存在
        if( !sceneValidator.hasScene(targetSceneId)){
            return Result.valueOf("场景不存在");
        }

        // 2 能否移动到target场景
        if(!sceneValidator.canMoveToScene(actorId,targetSceneId)){
            return Result.valueOf("无法移动到目标场景");
        }

        return sceneManager.moveToScene(actorId,targetSceneId);
    }

    /**
     * 进入场景
     * @param actorId
     * @return
     */
    public Result<?> enter(long actorId) {
        userManager.getUser()
        if(){

        }
        return sceneManager.enter(actorId);
    }
//
//    public Result<?> aoi(long actorId, JSONObject jsonObject) {
//
//        SceneDo sceneDo = sceneManager.getSceneDo(actorId);
//        SceneDTO sceneDTO = new SceneDTO(sceneDo);
//        boolean closeOwn = jsonObject.getBooleanValue("closeOwn");
//        if( closeOwn){
//            sceneDTO.getActorIdSet().remove(actorId);
//        }
//        return Result.success(sceneDTO);
//    }

    /**
     * 对话NPC
     * @param actorId
     * @param npcId
     * @param jsonObject
     * @return
     */
    public Result<?> talk(long actorId, int npcId, JSONObject jsonObject) {
        // 1 当前场景是否存在
        if( !sceneValidator.hasNPC(actorId,npcId)){
            return Result.valueOf("当前场景不存在此npc");
        }

        return Result.success("和npc对话");
    }
}
