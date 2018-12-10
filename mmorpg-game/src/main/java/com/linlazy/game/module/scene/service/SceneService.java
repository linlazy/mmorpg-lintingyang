package com.linlazy.game.module.scene.service;


import com.linlazy.game.module.common.Result;
import com.linlazy.game.module.scene.config.SceneConfigService;
import com.linlazy.game.module.scene.dao.SceneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 玩家场景服务类
 */
@Component
public class SceneService {

    @Autowired
    private SceneConfigService sceneConfigService;
    @Autowired
    private SceneDao sceneDao;

    /**
     * 移动到某个场景
     * @param targetSceneId
     */
    public Result<?> moveTo(int targetSceneId){

//        // 1 target场景存在
//        if( !sceneConfigService.hasScene(targetSceneId)){
//            return Result.valueOf(SceneCode.SCENE_NOT_MOVE);
//        }
//
//        // 2 玩家所处场景可以移动到target场景
//        // 获取玩家当前所处场景Id
//        int currentSceneId = sceneDao.getCurrnetSceneId();
//        if(!sceneConfigService.canMoveToTarget(currentSceneId,targetSceneId)){
//            return Result.valueOf(SceneCode.SCENE_NOT_MOVE);
//        }
//        // 3 更新玩家所处场景为targetSceneId
//        sceneDao.updateCurrentSceneId(targetSceneId);
        return Result.success();
    }
}
