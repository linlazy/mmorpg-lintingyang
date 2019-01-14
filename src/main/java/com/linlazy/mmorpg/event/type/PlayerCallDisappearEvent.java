package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.PlayerCall;
import lombok.Data;

/**
 * 玩家召唤兽消失事件
 * @author linlazy
 */
@Data
public class PlayerCallDisappearEvent {

    private PlayerCall playerCall;

    public PlayerCallDisappearEvent(PlayerCall playerCall) {
        this.playerCall = playerCall;
    }
}
