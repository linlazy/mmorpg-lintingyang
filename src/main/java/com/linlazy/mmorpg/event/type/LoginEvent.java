package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.player.domain.Player;
import lombok.Data;

/**
 * 登录事件
 * @author linlazy
 */
@Data
public class LoginEvent {
    private Player player;

    public LoginEvent(Player player) {
        this.player = player;
    }
}
