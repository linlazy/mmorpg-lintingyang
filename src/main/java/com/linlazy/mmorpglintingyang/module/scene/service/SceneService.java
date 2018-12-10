package com.linlazy.mmorpglintingyang.module.scene.service;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneCode;
import com.linlazy.mmorpglintingyang.module.scene.dao.SceneDao;
import com.linlazy.mmorpglintingyang.module.scene.entity.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

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

        // 1 target场景存在
        if( !sceneConfigService.hasScene(targetSceneId)){
            return Result.valueOf(SceneCode.SCENE_NOT_EXIST);
        }

        // 2 玩家所处场景可以移动到target场景
        // 获取玩家当前所处场景Id
        Scene scene = sceneDao.getScene(4194306);
        int currentSceneId = scene.getSceneId();
        if(!sceneConfigService.canMoveToTarget(currentSceneId,targetSceneId)){
            return Result.valueOf(SceneCode.SCENE_NOT_MOVE);
        }
        // 3 更新玩家所处场景为targetSceneId
        scene.setSceneId(targetSceneId);
        sceneDao.updateScene(scene);
        return Result.success();
    }

    /**
     * 获取场景信息
     * @return
     */
    public Result<?> info() {
        Collection<JSONObject> info = sceneConfigService.getInfo();
        return Result.success(info);
    }
}
