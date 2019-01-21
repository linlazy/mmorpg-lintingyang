package com.linlazy.mmorpg.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linlazy
 */
@Data
public class Transaction {
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
    private List<ItemContext> inviterItemContextList = new ArrayList<>();

    /**
     * 接受者交易道具
     */
    private List<ItemContext> acceptorItemContextList = new ArrayList<>();

}
