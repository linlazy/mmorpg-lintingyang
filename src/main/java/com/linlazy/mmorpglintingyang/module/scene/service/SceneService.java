package com.linlazy.mmorpglintingyang.module.scene.service;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneCode;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityTypeStatus;
import com.linlazy.mmorpglintingyang.module.scene.dao.SceneDao;
import com.linlazy.mmorpglintingyang.module.scene.dao.SceneEntityDao;
import com.linlazy.mmorpglintingyang.module.scene.entity.Scene;
import com.linlazy.mmorpglintingyang.module.scene.entity.SceneEntity;
import com.linlazy.mmorpglintingyang.module.scene.entity.model.SceneEntityInfo;
import com.linlazy.mmorpglintingyang.module.user.dao.UserDao;
import com.linlazy.mmorpglintingyang.module.user.entity.User;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
    @Autowired
    private UserDao userDao;

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
        //初始化场景实体
        if(sceneEntity == null){
            sceneEntity = new SceneEntity();
            sceneEntity.setSceneId(scene.getSceneId());

            //获取场景怪物初始化信息
            List<JSONObject> sceneMonsterList = sceneConfigService.getMonsterBySceneId(scene.getSceneId());
            for(int i = 0 ; i < sceneMonsterList.size(); i ++){
                JSONObject sceneMonster = sceneMonsterList.get(i);
                int monsterId = sceneMonster.getIntValue("monsterId");
                int hp = sceneMonster.getIntValue("hp");

                sceneEntity.getEntitySet().add(new SceneEntityInfo(monsterId,SceneEntityType.Monster, SceneEntityTypeStatus.EXISTENCE,hp));
            }

            //玩家自身
            User user = userDao.getUser(actorId);
            sceneEntity.getEntitySet().add(new SceneEntityInfo(actorId,SceneEntityType.Player,user.getStatus(),user.getHp()));

            //存档
            sceneEntity.setEntities(JSONObject.toJSONString(sceneEntity.getEntitySet()));
            sceneEntityDao.addSceneEntity(sceneEntity);
        }
        Set<SceneEntityInfo> entitySet = sceneEntity.getEntitySet();

        return entitySet;
    }


    /**
     * 更新场景实体
     * @param sceneId
     * @param sceneEntityInfo
     */
    public void updateSceneEntityInfo(int sceneId,SceneEntityInfo sceneEntityInfo){
        SceneEntity sceneEntity = sceneEntityDao.getSceneEntity(sceneId);
        Set<SceneEntityInfo> entitySet = sceneEntity.getEntitySet();
        if(entitySet.contains(sceneEntityInfo)){
            entitySet.remove(sceneEntityInfo);
            entitySet.add(sceneEntityInfo);
            sceneEntity.setEntities(JSONObject.toJSONString(entitySet));
            sceneEntityDao.updateSceneEntity(sceneEntity);
        }
    }

    public Scene getScene(long actorId) {
        return sceneDao.getScene(actorId);
    }

    /**
     * 获取怪物当前信息
     * @param actorId
     * @param monsterId
     * @return
     */
    public SceneEntityInfo getMonsterInfo(long actorId,int monsterId) {

        Set<SceneEntityInfo> currentInfo = this.getCurrentInfo(actorId);
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
            Scene scene = sceneDao.getScene(actorId);
            if(scene != null && scene.getSceneId() == sceneId){
                resultSet.add(actorId);
            }
        }
        return resultSet;
    }
}
