package com.linlazy.mmorpglintingyang.module.item.validator;

import com.linlazy.mmorpglintingyang.module.transaction.manager.TransactionsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class ItemValidator {

    @Autowired
    private TransactionsManager transactionsManager;

    /**
     * 当前物品处于交易状态
     * @param actorId
     * @return
     */
    public boolean isLock(long actorId){
        return transactionsManager.isLock(actorId);
    }
}
