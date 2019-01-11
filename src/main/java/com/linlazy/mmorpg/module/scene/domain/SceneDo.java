package com.linlazy.mmorpg.module.scene.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author linlazy
 */
@Data
public class SceneDo {
    /**
     * 场景ID
     */
    private int  sceneId;

    /**
     * 副本ID
     */
    private int copyId;

    /**
     * 场景所在玩家集合
     */
    private Set<Long> actorIdSet = new HashSet<>();

    /**
     * 场景实体集合（非玩家）
     */
    private Set<SceneEntity> sceneEntitySet = new HashSet<>();

    public Set<SceneEntity> getSceneEntitySet() {
        return sceneEntitySet;
    }

    public void setSceneEntitySet(Set<SceneEntity> sceneEntitySet) {
        this.sceneEntitySet = sceneEntitySet;
    }
}
