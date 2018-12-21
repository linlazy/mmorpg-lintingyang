package com.linlazy.mmorpglintingyang.module.scene.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SceneDo {
    /**
     * 场景ID
     */
    private int  sceneId;

    /**
     * 场景所在玩家集合
     */
    private Set<Long> actorIdSet = new HashSet<>();

    /**
     * 场景实体集合（非玩家）
     */
    private Set<SceneEntityDo> sceneEntityDoSet = new HashSet<>();
}
