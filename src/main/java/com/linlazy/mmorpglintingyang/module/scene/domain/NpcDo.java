package com.linlazy.mmorpglintingyang.module.scene.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 谈话进度
     */
    private Map<Integer, List<String>> talkProcess = new HashMap<>();

}
