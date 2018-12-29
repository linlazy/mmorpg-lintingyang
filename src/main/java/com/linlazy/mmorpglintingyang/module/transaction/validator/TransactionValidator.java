package com.linlazy.mmorpglintingyang.module.transaction.validator;

import com.linlazy.mmorpglintingyang.module.transaction.manager.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidator {

    @Autowired
    private TransactionManager transactionManager;


    /**
     * 玩家是否处于正在交易状态
     * @param actorId
     * @param targetId
     * @return
     */
    public boolean isTrading(long actorId,long targetId){
        return transactionManager.isTrading(actorId) ||transactionManager.isTrading(targetId);
    }
}
