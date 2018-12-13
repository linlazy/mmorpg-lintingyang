package com.linlazy.mmorpglintingyang.module.scene.service;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneCode;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.scene.manager.entity.Scene;
import com.linlazy.mmorpglintingyang.module.scene.manager.entity.model.SceneEntityInfo;
import com.linlazy.mmorpglintingyang.module.user.service.UserService;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 玩家场景服务类
 */
@Component
public class SceneService {

    @Autowired
    private SceneManager sceneManager;

    @Autowired
    private UserService userService;

    /**
     * 移动到某个场景
     * @param targetSceneId
     */
    public Result<?> moveTo(long actorId,int targetSceneId){

        // 1 target场景存在
        if( !sceneManager.hasScene(targetSceneId)){
            return Result.valueOf(SceneCode.SCENE_NOT_EXIST);
        }

        // 2 玩家所处场景可以移动到target场景
        if(!sceneManager.canMoveToTarget(actorId,targetSceneId)){
            return Result.valueOf(SceneCode.SCENE_NOT_MOVE);
        }


        SceneEntityInfo sceneEntityInfo = new SceneEntityInfo(actorId, SceneEntityType.Player);
        Scene scene = sceneManager.getScene(actorId);
        //原场景中移除实体
        sceneManager.removeSceneEntityInfo(scene.getSceneId(),sceneEntityInfo);
        //新场景中增加实体
        sceneManager.addSceneEntityInfo(targetSceneId,sceneEntityInfo);

        // 4 更新玩家所处场景为targetSceneId
        scene.setSceneId(targetSceneId);
        sceneManager.updateScene(scene);
        return Result.success();
    }

    /**
     * 获取所有场景信息
     * @return
     */
    public Result<?> getAllConfigInfo() {
        Collection<JSONObject> info = sceneManager.getAllConfigInfo();
        return Result.success(info);
    }


    /**
     * 进入场景
     * @param actorId
     * @return
     */
    public Result<?> enter(long actorId) {
        //场景实体变化
        Scene scene = sceneManager.getScene(actorId);
        SceneEntityInfo sceneEntityInfo = new SceneEntityInfo(actorId, SceneEntityType.Player);
        sceneManager.addSceneEntityInfo(scene.getSceneId(),sceneEntityInfo);
        return Result.success();
    }

    /**
     * 获取当前场景实体信息
     * @param actorId
     * @return
     */
    public Set<SceneEntityInfo> getCurrentInfo(long actorId) {
        //获取当前场景怪物实体信息
        Set<SceneEntityInfo> sceneEntityInfoSet = sceneManager.getCurrentMonsterInfo(actorId);

        return sceneEntityInfoSet;
    }


    /**
     * 更新场景实体
     * @param sceneId
     * @param sceneEntityInfo
     */
    public void updateSceneEntityInfo(int sceneId,SceneEntityInfo sceneEntityInfo){
        sceneManager.updateSceneEntityInfo(sceneId,sceneEntityInfo);
    }

    public Scene getScene(long actorId) {
        return sceneManager.getScene(actorId);
    }

    /**
     * 获取怪物当前信息
     * @param actorId
     * @param monsterId
     * @return
     */
    public SceneEntityInfo getMonsterInfo(long actorId,int monsterId) {

        Set<SceneEntityInfo> currentInfo = sceneManager.getCurrentMonsterInfo(actorId);
        for(SceneEntityInfo sceneEntityInfo: currentInfo){
            if(sceneEntityInfo.getEntityId() == monsterId
                    && sceneEntityInfo.getEntityType() == SceneEntityType.Monster){
                return sceneEntityInfo;
            }
        }
        return null;
    }


    public Set<Long> getCurrentSceneOnlineActorIds(int sceneId) {
        Set<Long> resultSet = new HashSet<>();

        Set<Long> onlineActorIds = SessionManager.getOnlineActorIds();
        for(Long actorId: onlineActorIds){
            Scene scene = sceneManager.getScene(actorId);
            if(scene != null && scene.getSceneId() == sceneId){
                resultSet.add(actorId);
            }
        }
        return resultSet;
    }
}
