package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.Player;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class PlayerEvent {

    protected Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }
}
