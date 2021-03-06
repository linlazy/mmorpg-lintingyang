package com.linlazy.mmorpg.module.scene.domain;

import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linlazy
 */
@Data
public class Npc extends SceneEntity {

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
     * 当前血量
     */
    private int hp;

    /**
     * 谈话进度
     */
    private Map<Integer, List<String>> talkProcess = new HashMap<>();

    @Override
    public int computeDefense() {
        return 0;
    }

    @Override
    public int computeAttack() {
        return 0;
    }
}
