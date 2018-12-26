package com.linlazy.mmorpglintingyang.module.scene.service;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneDo;
import com.linlazy.mmorpglintingyang.module.scene.dto.SceneDTO;
import com.linlazy.mmorpglintingyang.module.scene.manager.NPCManager;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.scene.validator.SceneValidator;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 玩家场景服务类
 */
@Component
public class SceneService {

    @Autowired
    private SceneManager sceneManager;

    @Autowired
    private SceneValidator sceneValidator;

    @Autowired
    private NPCManager npcManager;


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
       return sceneManager.enter(actorId);
    }

    public Result<?> aoi(long actorId, JSONObject jsonObject) {

        SceneDo sceneDo = sceneManager.getSceneDo(actorId);
        SceneDTO sceneDTO = new SceneDTO(sceneDo);
        if(jsonObject.getBooleanValue("closeOwn")) {
            sceneDTO.getActorIdSet().remove(actorId);
        }
        return Result.success(sceneDTO);
    }

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
