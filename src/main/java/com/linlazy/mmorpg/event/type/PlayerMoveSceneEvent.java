package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.Player;

/**
 * @author linlazy
 */
public class PlayerMoveSceneEvent extends PlayerEvent{

    public PlayerMoveSceneEvent(Player player) {
        super(player);
    }
}
