package com.linlazy.mmorpglintingyang.module.domain;

import com.linlazy.mmorpglintingyang.module.email.entity.Email;

import java.util.HashSet;
import java.util.Set;

/**
 * 玩家邮件信息
 * @author linlazy
 */
public class PlayerEmailInfo {

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 玩家邮件集合
     */
    Set<Email> emailSet = new HashSet<>();
}
