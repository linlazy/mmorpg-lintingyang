package com.linlazy.mmorpglintingyang.module.scene.manager.domain;


import java.util.Set;
import java.util.stream.Collectors;

/**
 * 场景怪物
 */
public class SceneMonster {

    /**
     * 场景中可受攻击的实体
     */
    private Set<SceneEntityDo> sceneEntityDoSet;

    public SceneMonster(int sceneId) {
    }


    /**
     *  场景实体受到攻击
     * @param sceneEntityIdSet 受攻击的场景实体ID集合
     * @param attacked 受到的伤害
     */
    public void attacked(Set<Long> sceneEntityIdSet,int attacked){

        //实际受到攻击的实体集合
        Set<SceneEntityDo> actuallySceneEntityDoSet = sceneEntityDoSet.stream()
                .filter(sceneEntityDo ->
                        sceneEntityIdSet.contains(sceneEntityDo.getSceneEntityId()))
                .collect(Collectors.toSet());

        for(SceneEntityDo sceneEntityDo: actuallySceneEntityDoSet){
            int hp = sceneEntityDo.getHp() - attacked;
            sceneEntityDo.setHp(hp);
        }
    }
}
