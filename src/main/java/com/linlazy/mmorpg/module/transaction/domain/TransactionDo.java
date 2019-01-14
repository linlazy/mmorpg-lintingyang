package com.linlazy.mmorpg.module.transaction.domain;

import lombok.Data;

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

//    /**
//     * 邀请者交易道具
//     */
//    private Set<ItemDo> inviterItemDoSet = new HashSet<>();
//
//    /**
//     * 接受者交易道具
//     */
//    private Set<ItemDo> acceptorItemDoSet = new HashSet<>();

}
