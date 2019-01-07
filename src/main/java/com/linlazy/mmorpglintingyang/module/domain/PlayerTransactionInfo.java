package com.linlazy.mmorpglintingyang.module.domain;

import java.util.Set;

/**
 * 玩家交易信息
 * @author linlazy
 */
public class PlayerTransactionInfo {

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 是否锁定交易
     */
    private boolean lock;

    /**
     * 玩家交易物品
     */
    private Set<Item> itemSet;

    /**
     * 确认交易
     */
    private boolean enter;
}
