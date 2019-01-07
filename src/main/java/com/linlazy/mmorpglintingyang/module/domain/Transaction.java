package com.linlazy.mmorpglintingyang.module.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易领域类
 * @author linlazy
 */
public class Transaction {

    /**
     * 交易ID
     */
    private long transactionId;

    Map<Long, TransactionPlayerInfo> transactionPlayerInfoMap = new HashMap<>();
}
