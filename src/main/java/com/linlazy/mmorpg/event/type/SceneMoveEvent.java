package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.Player;
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
