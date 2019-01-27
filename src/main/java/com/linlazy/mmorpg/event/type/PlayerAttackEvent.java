package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.player.domain.Player;

/**
 * @author linlazy
 */
public class PlayerAttackEvent extends PlayerEvent{

    public PlayerAttackEvent(Player player) {
        super(player);
    }
}
