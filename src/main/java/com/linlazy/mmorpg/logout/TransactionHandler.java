package com.linlazy.mmorpg.logout;

import com.linlazy.mmorpg.server.common.LogoutListener;
import com.linlazy.mmorpg.service.TransactionService;

/**
 * 交易登出处理器
 * @author linlazy
 */
public class TransactionHandler implements LogoutListener {

    private TransactionService transactionService;

    @Override
    public void logout(long actorId) {
        transactionService.clear(actorId);
    }
}
