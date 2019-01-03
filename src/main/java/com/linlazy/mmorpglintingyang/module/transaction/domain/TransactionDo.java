package com.linlazy.mmorpglintingyang.module.transaction.domain;

import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author linlazy
 */
@Data
public class TransactionDo {
    /**
     * 交易号
     */
    private long transactionId;

    /**
     * 邀请者
     */
    private long inviter;
    /**
     * 接受者
     */
    private long acceptor;
    /**
     * 邀请者锁定
     */
    private boolean inviterLock;

    /**
     * 接受者锁定
     */
    private boolean acceptorLock;

    /**
     * 邀请者确认
     */
    private boolean inviterEnter;

    /**
     * 接受者确认
     */
    private boolean acceptorEnter;

    /**
     * 邀请者交易道具
     */
    private Set<ItemDo> inviterItemDoSet = new HashSet<>();

    /**
     * 接受者交易道具
     */
    private Set<ItemDo> acceptorItemDoSet = new HashSet<>();

}
