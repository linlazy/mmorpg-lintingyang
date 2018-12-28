package com.linlazy.mmorpglintingyang.module.scene.domain;

import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import lombok.Data;

@Data
public class ScenePlayerDo {


    private int sceneId;
    private long actorId;
    private String name;
    private int hp;
    private int copyId;

    public ScenePlayerDo(User user) {
        this.sceneId = user.getSceneId();
        this.actorId = user.getActorId();
        this.name = user.getUsername();
        this.hp =user.getHp();
    }
}
