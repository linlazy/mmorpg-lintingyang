package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.player.domain.Player;

/**
 * @author linlazy
 */
public class PlayerAttackedEvent extends PlayerEvent{

    public PlayerAttackedEvent(Player player) {
        super(player);
    }
}
