package com.linlazy.mmorpglintingyang.module.user.entity;

import lombok.Data;

@Data
public class User {

    /**
     * 唯一标示
     */
    private long actorId;

    /**
     * 令牌
     */
    private String token;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 状态
     */
    private int status;
    /**
     * 红
     */
    private int hp;
    /**
     * 蓝
     */
    private int mp;
}
