package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Player;
import lombok.Data;

/**
 * 玩家信息
 * @author linlazy
 */
@Data
public class PlayerDTO {

    /**
     * 用户名
     */
    private String username;
    /**
     * 血量
     */
    private Integer hp;

    public PlayerDTO(Player player) {
        username = player.getName();
        hp = player.getHp();
    }


    @Override
    public String toString() {
        return "PlayerDTO{" +
                "username='" + username + '\'' +
                ", hp=" + hp +
                '}';
    }
}
