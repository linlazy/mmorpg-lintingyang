package com.linlazy.mmorpg.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 场景怪物信息
 * @author linlazy
 */
public class SceneMonsterInfo {

    /**
     * 场景ID
     */
    private long sceneId;

    /**
     * 场景怪物信息
     */
    private Set<Monster> monsterSet = new HashSet<>();
}
