package com.linlazy.mmorpg.domain;


import com.linlazy.mmorpg.entity.EmailEntity;

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
    Set<EmailEntity> emailSet = new HashSet<>();
}
