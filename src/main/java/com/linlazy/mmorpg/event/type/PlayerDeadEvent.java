package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.player.domain.Player;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class PlayerDeadEvent  {

    private Player player;

    public PlayerDeadEvent(Player player) {
        this.player = player;
    }
}
