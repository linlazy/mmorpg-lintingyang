package com.linlazy.mmorpg.module.transaction.validator;

import com.linlazy.mmorpg.module.transaction.manager.TransactionsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class TransactionValidator {

    @Autowired
    private TransactionsManager transactionsManager;


    /**
     * 玩家是否处于正在交易状态
     * @param actorId
     * @param targetId
     * @return
     */
    public boolean isTrading(long actorId,long targetId){
        return transactionsManager.isTrading(actorId) || transactionsManager.isTrading(targetId);
    }

}
