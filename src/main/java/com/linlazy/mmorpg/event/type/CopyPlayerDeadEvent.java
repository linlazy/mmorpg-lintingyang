package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.Player;

/**
 * @author linlazy
 */
public class CopyPlayerDeadEvent extends PlayerDeadEvent{

    public CopyPlayerDeadEvent(Player player) {
        super(player);
    }
}
