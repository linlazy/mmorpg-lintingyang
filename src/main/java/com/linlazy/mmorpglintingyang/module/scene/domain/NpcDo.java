package com.linlazy.mmorpglintingyang.module.scene.domain;

import lombok.Data;

@Data
public class NpcDo {

    /**
     * 场景Id
     */
    private int sceneId;

    /**
     * npcID
     */
    private int npcId;

    /**
     * 名称
     */
    private String name;

}
