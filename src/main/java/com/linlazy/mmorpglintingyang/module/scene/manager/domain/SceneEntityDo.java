package com.linlazy.mmorpglintingyang.module.scene.manager.domain;

import com.linlazy.mmorpglintingyang.module.scene.manager.entity.SceneEntity;
import lombok.Data;

@Data
public class SceneEntityDo {

    /**
     * 场景实体ID
     */
    private long  sceneEntityId;


    private int hp;


    public SceneEntityDo(SceneEntity sceneEntity) {
    }
}
