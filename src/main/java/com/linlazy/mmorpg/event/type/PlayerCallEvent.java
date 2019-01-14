package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.PlayerCall;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class PlayerCallEvent {
    PlayerCall playerCall;

    public PlayerCallEvent(PlayerCall playerCall) {
        this.playerCall = playerCall;
    }
}
