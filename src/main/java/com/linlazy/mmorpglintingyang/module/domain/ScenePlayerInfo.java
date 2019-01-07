package com.linlazy.mmorpglintingyang.module.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 场景玩家信息
 * @author linlazy
 */
public class ScenePlayerInfo {

    /**
     * 场景ID
     */
    private long sceneId;

    /**
     * 场景玩家信息
     */
    Set<Player> playerSet = new HashSet<>();

}
