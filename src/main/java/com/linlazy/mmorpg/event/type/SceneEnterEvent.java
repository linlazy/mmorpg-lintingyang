package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.player.domain.Player;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class SceneEnterEvent {
    private Player player;

    public SceneEnterEvent(Player player) {
        this.player = player;
    }
}
