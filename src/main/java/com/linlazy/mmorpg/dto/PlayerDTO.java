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

    /**
     * 等级
     */
    private Integer level;

    public PlayerDTO(Player player) {
        username = player.getName();
        hp = player.getHp();
        level = player.getLevel();
    }


    @Override
    public String toString() {
        return String.format("玩家 【%s】血量：%d 等级：【%d】",username,hp,level);
    }
}
