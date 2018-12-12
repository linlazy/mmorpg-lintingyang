package com.linlazy.mmorpglintingyang.module.scene.service;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneCode;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.module.scene.dao.SceneDao;
import com.linlazy.mmorpglintingyang.module.scene.dao.SceneEntityDao;
import com.linlazy.mmorpglintingyang.module.scene.entity.Scene;
import com.linlazy.mmorpglintingyang.module.scene.entity.SceneEntity;
import com.linlazy.mmorpglintingyang.module.scene.entity.model.SceneEntityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 * 玩家场景服务类
 */
@Component
public class SceneService {

    @Autowired
    private SceneConfigService sceneConfigService;
    @Autowired
    private SceneDao sceneDao;
    @Autowired
    private SceneEntityDao sceneEntityDao;


    /**
     * 移动到某个场景
     * @param targetSceneId
     */
    public Result<?> moveTo(long actorId,int targetSceneId){

        // 1 target场景存在
        if( !sceneConfigService.hasScene(targetSceneId)){
            return Result.valueOf(SceneCode.SCENE_NOT_EXIST);
        }

        // 2 玩家所处场景可以移动到target场景
        // 获取玩家当前所处场景Id
        Scene scene = sceneDao.getScene(actorId);
        if(!sceneConfigService.canMoveToTarget(scene.getSceneId(),targetSceneId)){
            return Result.valueOf(SceneCode.SCENE_NOT_MOVE);
        }
        // 3 更新玩家所处场景为targetSceneId
        scene.setSceneId(targetSceneId);
        sceneDao.updateScene(scene);

        //场景实体变化
            //原场景
            SceneEntity oldSceneEntity = sceneEntityDao.getSceneEntity(scene.getSceneId());
            oldSceneEntity.getEntitySet().remove(new SceneEntityInfo(actorId,SceneEntityType.Player));
            sceneEntityDao.updateSceneEntity(oldSceneEntity);
            //目标场景
            SceneEntity targetSceneEntity = sceneEntityDao.getSceneEntity(targetSceneId);
            targetSceneEntity.getEntitySet().add(new SceneEntityInfo(actorId,SceneEntityType.Player));
            sceneEntityDao.updateSceneEntity(targetSceneEntity);
        return Result.success();
    }

    /**
     * 获取所有场景信息
     * @return
     */
    public Result<?> getAllInfo() {
        Collection<JSONObject> info = sceneConfigService.getAllInfo();
        return Result.success(info);
    }


    /**
     * 进入场景
     * @param actorId
     * @return
     */
    public Result<?> enter(long actorId) {
        Scene scene = sceneDao.getScene(actorId);
        //初始化场景
        if(scene == null){
            scene = new Scene();
            scene.setActorId(actorId);
            scene.setSceneId(sceneConfigService.getInitSceneId());
            sceneDao.addScene(scene);
        }

        //场景实体变化
        SceneEntity sceneEntity = sceneEntityDao.getSceneEntity(scene.getSceneId());
        sceneEntity.getEntitySet().add(new SceneEntityInfo(actorId,SceneEntityType.Player));
        sceneEntityDao.updateSceneEntity(sceneEntity);
        return Result.success();
    }

    /**
     * 获取当前场景实体信息
     * @param actorId
     * @return
     */
    public Set<SceneEntityInfo> getCurrentInfo(long actorId) {

        Scene scene = sceneDao.getScene(actorId);
        //初始化场景
        if(scene == null){
            scene = new Scene();
            scene.setActorId(actorId);
            scene.setSceneId(sceneConfigService.getInitSceneId());
            sceneDao.addScene(scene);
        }

        SceneEntity sceneEntity = sceneEntityDao.getSceneEntity(scene.getSceneId());
        if(sceneEntity == null){
            sceneEntity = new SceneEntity();
            sceneEntity.setSceneId(scene.getSceneId());
        }
        Set<SceneEntityInfo> entitySet = sceneEntity.getEntitySet();

        return entitySet;
    }

    /**
     * 对话NPC
     * @param actorId
     * @param npcId
     * @return
     */
    public Result<?> talkNPC(long actorId,int npcId) {
        return Result.success("你好我是");
    }
}
