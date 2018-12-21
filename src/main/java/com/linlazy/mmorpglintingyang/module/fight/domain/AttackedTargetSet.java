package com.linlazy.mmorpglintingyang.module.fight.domain;

import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import lombok.Data;

import java.util.Set;

@Data
public class AttackedTargetSet {

    private Set<SceneEntityDo> sceneEntityDos;

    public void attacked(int attack) {

    }
}
