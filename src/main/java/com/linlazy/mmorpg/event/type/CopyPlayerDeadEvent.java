package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.Player;
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
