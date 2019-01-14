package com.linlazy.mmorpg.dto;

/**
 * 玩家信息
 * @author linlazy
 */
public class PlayerDTO {

    /**
     * 用户名
     */
    private String username;
    /**
     * 血量
     */
    private Integer hp;

    public PlayerDTO(String username, Integer hp) {
        this.username = username;
        this.hp = hp;
    }


    @Override
    public String toString() {
        return "PlayerDTO{" +
                "username='" + username + '\'' +
                ", hp=" + hp +
                '}';
    }
}
