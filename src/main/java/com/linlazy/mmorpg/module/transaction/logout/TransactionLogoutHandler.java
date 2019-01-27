package com.linlazy.mmorpg.module.transaction.logout;

import com.linlazy.mmorpg.server.common.LogoutListener;
import com.linlazy.mmorpg.module.transaction.service.TransactionService;

/**
 * 交易登出处理器
 * @author linlazy
 */
public class TransactionLogoutHandler implements LogoutListener {

    private TransactionService transactionService;

    @Override
    public void logout(long actorId) {
        transactionService.clear(actorId);
    }
}
