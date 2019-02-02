package com.linlazy.mmorpg.module.transaction.domain;

import com.linlazy.mmorpg.module.item.domain.Item;
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
    private List<Item> inviterItemList = new ArrayList<>();

    /**
     * 接受者交易道具
     */
    private List<Item> acceptorItemList = new ArrayList<>();

}
