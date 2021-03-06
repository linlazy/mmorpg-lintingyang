package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.player.domain.Player;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class CopyMoveEvent {
    private Player player;

    public CopyMoveEvent(Player player) {
        this.player = player;
    }
}
