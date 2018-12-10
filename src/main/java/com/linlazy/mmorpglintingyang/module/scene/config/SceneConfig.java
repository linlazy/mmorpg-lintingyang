package com.linlazy.mmorpglintingyang.module.scene.config;


import java.util.HashSet;
import java.util.Set;

/**
 * 游戏场景配置类
 */
public class SceneConfig {


    /**
     * 场景ID
     */
    private int sceneId;
    /**
     * 场景名称
     */
    private String name;

    /**
     *  邻居场景ID集合
     */
    private Set<Integer> neighborSet = new HashSet<>();


    public SceneConfig(int sceneId, String name, Set<Integer> neighborSet) {
        this.sceneId = sceneId;
        this.name = name;
        this.neighborSet = neighborSet;
    }


    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Integer> getNeighborSet() {
        return neighborSet;
    }

    public void setNeighborSet(Set<Integer> neighborSet) {
        this.neighborSet = neighborSet;
    }
}
