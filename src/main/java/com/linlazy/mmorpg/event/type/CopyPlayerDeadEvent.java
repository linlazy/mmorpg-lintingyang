package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.player.domain.Player;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class CopyPlayerDeadEvent {

    private Player player;

    public CopyPlayerDeadEvent(Player player) {
        this.player = player;
    }
}
