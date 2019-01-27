package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.player.domain.Player;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class SceneMoveEvent {
    private Player player;

    public SceneMoveEvent( Player player) {
        this.player = player;
    }
}
