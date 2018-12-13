package com.linlazy.mmorpglintingyang.module.scene.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.scene.manager.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityTypeStatus;
import com.linlazy.mmorpglintingyang.module.scene.manager.dao.SceneDao;
import com.linlazy.mmorpglintingyang.module.scene.manager.dao.SceneEntityDao;
import com.linlazy.mmorpglintingyang.module.scene.manager.entity.Scene;
import com.linlazy.mmorpglintingyang.module.scene.manager.entity.SceneEntity;
import com.linlazy.mmorpglintingyang.module.scene.manager.entity.model.SceneEntityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class SceneManager {
    @Autowired
    private SceneConfigService sceneConfigService;
    @Autowired
    private SceneDao sceneDao;
    @Autowired
    private SceneEntityDao sceneEntityDao;

    /**
     * 是否有此场景
     * @param targetSceneId
     * @return
     */
    public boolean hasScene(int targetSceneId) {
        return sceneConfigService.hasScene(targetSceneId);
    }

    /**
     * 获取场景
     * @param actorId
     * @return
     */
    public Scene getScene(long actorId) {
        Scene scene = sceneDao.getScene(actorId);
        //初始化场景
        if(scene == null){
            scene = new Scene();
            scene.setActorId(actorId);
            scene.setSceneId(sceneConfigService.getInitSceneId());
            sceneDao.addScene(scene);
        }
        return scene;
    }

    /**
     * 能否移动到当前场景
     * @param actorId
     * @param targetSceneId
     * @return
     */
    public boolean canMoveToTarget(long actorId, int targetSceneId) {
        Scene scene = this.getScene(actorId);
        return sceneConfigService.canMoveToTarget(scene.getSceneId(),targetSceneId);
    }

    /**
     * 更新场景
     * @param scene
     */
    public void updateScene(Scene scene) {
        sceneDao.updateScene(scene);
    }

    /**
     * 移除场景实体
     * @param sceneId
     * @param sceneEntityInfo
     */
    public void removeSceneEntityInfo(int sceneId, SceneEntityInfo sceneEntityInfo){
        SceneEntity sceneEntity = sceneEntityDao.getSceneEntity(sceneId);
        sceneEntity.getEntitySet().remove(sceneEntityInfo);
        sceneEntityDao.updateSceneEntity(sceneEntity);
    }

    /**
     * 新增场景实体
     * @param sceneId
     * @param sceneEntityInfo
     */
    public void addSceneEntityInfo(int sceneId, SceneEntityInfo sceneEntityInfo){
        SceneEntity sceneEntity = sceneEntityDao.getSceneEntity(sceneId);
        sceneEntity.getEntitySet().add(sceneEntityInfo);
        sceneEntityDao.updateSceneEntity(sceneEntity);
    }

    public SceneEntity getSceneEntity(int sceneId) {
        SceneEntity sceneEntity = sceneEntityDao.getSceneEntity(sceneId);
        //初始化场景实体
        if(sceneEntity == null){
            sceneEntity = new SceneEntity();
            sceneEntity.setSceneId(sceneId);

            Set<SceneEntityInfo> entitySet = sceneEntity.getEntitySet();
            //获取场景怪物初始化信息
            List<JSONObject> sceneMonsterList = sceneConfigService.getMonsterBySceneId(sceneId);
            for(int i = 0 ; i < sceneMonsterList.size(); i ++){
                JSONObject sceneMonster = sceneMonsterList.get(i);
                int monsterId = sceneMonster.getIntValue("monsterId");
                int hp = sceneMonster.getIntValue("hp");

                entitySet.add(new SceneEntityInfo(monsterId, SceneEntityType.Monster, SceneEntityTypeStatus.EXISTENCE,hp));
            }
            //存档
            sceneEntity.setEntities(JSONObject.toJSONString(entitySet));
            sceneEntityDao.addSceneEntity(sceneEntity);
        }
        return sceneEntity;
    }

    /**
     * 获取当前场景实体信息
     * @param actorId
     * @return
     */
    public Set<SceneEntityInfo> getCurrentInfo(long actorId) {
        Scene scene = this.getScene(actorId);
        SceneEntity sceneEntity = this.getSceneEntity(scene.getSceneId());
        return sceneEntity.getEntitySet();
    }

    /**
     * 更新场景实体信息
     * @param sceneId
     * @param sceneEntityInfo
     */
    public void updateSceneEntityInfo(int sceneId, SceneEntityInfo sceneEntityInfo) {
        SceneEntity sceneEntity = this.getSceneEntity(sceneId);
        Set<SceneEntityInfo> entitySet = sceneEntity.getEntitySet();
        if(entitySet.contains(sceneEntityInfo)){
            entitySet.remove(sceneEntityInfo);
            entitySet.add(sceneEntityInfo);
            sceneEntity.setEntities(JSONObject.toJSONString(entitySet));
            sceneEntityDao.updateSceneEntity(sceneEntity);
        }
    }

    public Collection<JSONObject> getAllConfigInfo() {
        return sceneConfigService.getAllConfigInfo();
    }
}
