package com.linlazy.mmorpg.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 玩家聊天信息
 * @author linlazy
 */
public class PlayerChatInfo {

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 玩家聊天信息集合
     */
    private Set<Chat> chatSet = new HashSet<>();
}
