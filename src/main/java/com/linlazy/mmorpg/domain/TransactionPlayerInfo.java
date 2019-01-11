package com.linlazy.mmorpg.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易双方信息
 * @author linlazy
 */
public class TransactionPlayerInfo {

    /**
     * 交易ID
     */
    private long transactionId;

    /**
     * 交易双方信息
     */
    private List<PlayerTransactionInfo> playerTransactionInfos = new ArrayList<>(2);
}
