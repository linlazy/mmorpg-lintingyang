package com.linlazy.mmorpglintingyang.module.scene.domain;

import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import lombok.Data;

/**
 * 怪物领域类
 */
@Data
public class SceneEntityDo {


    /**
     * 场景ID
     */
    private int sceneId;

    /**
     * 场景实体ID
     */
    private long sceneEntityId;

    /**
     * 场景实体类型
     */
    private int sceneEntityType;


    /**
     * 是否死亡
     */
    private boolean isDead;

    /**
     * 实体名称
     */
    private String name;

    public SceneEntityDo(MonsterDo monsterDo) {
        this.sceneId = monsterDo.getSceneId();
        this.sceneEntityId = monsterDo.getMonsterId();
        this.name = monsterDo.getName();
        this.sceneEntityType = SceneEntityType.Monster;
    }

    public SceneEntityDo(NpcDo npcDo) {
        this.sceneId = npcDo.getSceneId();
        this.sceneEntityId = npcDo.getNpcId();
        this.name = npcDo.getName();
        this.sceneEntityType = SceneEntityType.Npc;
    }
}
