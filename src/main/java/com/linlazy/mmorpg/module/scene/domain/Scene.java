package com.linlazy.mmorpg.module.scene.domain;

import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.playercall.domain.PlayerCall;
import lombok.Data;

import java.util.*;

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
     * 相邻场景ID
     */
    private List<Integer> neighborSet = new ArrayList<>();

    /**
     * 场景玩家信息
     */
    protected Map<Long, Player> playerMap = new HashMap<>();

    /**
     * 场景怪物信息
     */
    protected Map<Long, Monster> monsterMap;

    /**
     * 场景召唤兽信息
     */
    protected Map<Long, PlayerCall> playerCallMap = new HashMap<>();

    /**
     * 场景BOSS信息
     */
    protected List<Boss> bossList;
    /**
     * 场景NPC信息
     */
    private Set<Npc> NpcSet = new HashSet<>();

}
