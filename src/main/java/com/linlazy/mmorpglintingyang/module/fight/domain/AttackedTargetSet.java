package com.linlazy.mmorpglintingyang.module.fight.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import lombok.Data;

import java.util.Set;

@Data
public class AttackedTargetSet {

    private Set<SceneEntityDo> sceneEntityDos;

    public void attacked(int damage, JSONObject jsonObject) {
        sceneEntityDos.stream()
                .forEach(sceneEntityDo -> sceneEntityDo.attacked(damage,jsonObject));
    }
}
