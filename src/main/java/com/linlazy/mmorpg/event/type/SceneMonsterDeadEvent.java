package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.Monster;
import com.linlazy.mmorpg.domain.Scene;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class SceneMonsterDeadEvent {
    private Scene scene;

    private Monster monster;

    public SceneMonsterDeadEvent(Scene scene, Monster monster) {
        this.scene = scene;
        this.monster = monster;
    }
}
