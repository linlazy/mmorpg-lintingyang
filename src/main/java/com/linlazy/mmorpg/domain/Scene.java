package com.linlazy.mmorpg.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 场景领域类
 * @author linlazy
 */
@Data
public class Scene {

    /**
     *  场景ID
     */
    private int sceneId;

    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 场景玩家信息
     */
    Set<Player> playerSet = new HashSet<>();

    /**
     * 场景怪物信息
     */
    private Set<Monster> monsterSet = new HashSet<>();

    /**
     * 场景BOSS信息
     */
    private Set<Boss> bossSet = new HashSet<>();
    /**
     * 场景NPC信息
     */
    private Set<Npc> NpcSet = new HashSet<>();

}
