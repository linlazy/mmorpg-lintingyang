package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.Player;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class PlayerDeadEvent extends PlayerEvent {
    public PlayerDeadEvent(Player player) {
        super(player);
    }
}
