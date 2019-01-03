package com.linlazy.mmorpglintingyang.module.scene.dto;

import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import lombok.Data;

import java.util.Set;

/**
 * @author linlazy
 */
@Data
public class SceneDTO {

    private final Integer sceneId;
    private final Set<Long> actorIdSet;
    private final Set<SceneEntityDo> sceneEntityDoSet;

    public SceneDTO(SceneDo sceneDo) {
       this.sceneId = sceneDo.getSceneId();
       this.actorIdSet =Sets.newHashSet(sceneDo.getActorIdSet());
       this.sceneEntityDoSet = Sets.newHashSet(sceneDo.getSceneEntityDoSet());
    }
}
