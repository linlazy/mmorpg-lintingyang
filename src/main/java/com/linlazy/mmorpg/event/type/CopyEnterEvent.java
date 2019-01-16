package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.Player;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class CopyEnterEvent {
    private Player player;

    public CopyEnterEvent(Player player) {
        this.player = player;
    }
}
