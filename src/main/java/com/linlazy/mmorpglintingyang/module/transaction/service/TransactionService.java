package com.linlazy.mmorpglintingyang.module.transaction.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.transaction.constants.TransactionOperatiorType;
import com.linlazy.mmorpglintingyang.module.transaction.manager.TransactionsManager;
import com.linlazy.mmorpglintingyang.module.transaction.push.TransactionPushHelper;
import com.linlazy.mmorpglintingyang.module.transaction.validator.TransactionValidator;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionService {

    @Autowired
    private TransactionValidator transactionValidator;

    @Autowired
    private TransactionsManager transactionsManager;

     /**
     * 邀请交易
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> inviteTrade(long actorId, long targetId) {
        if(transactionValidator.isTrading(actorId,targetId)){
            return Result.valueOf("玩家已处于交易状态");
        }
        return  transactionsManager.inviteTrade(actorId,targetId);
    }

    public Result<?> rejectTrade(long actorId, long targetId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourceId",actorId);
        TransactionPushHelper.pushTransactionOperation(targetId, TransactionOperatiorType.REJECT,jsonObject);
        return Result.success();
    }

    /**
     * 同意交易
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> acceptTrade(long actorId, long targetId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourceId",actorId);
        TransactionPushHelper.pushTransactionOperation(targetId, TransactionOperatiorType.ACCEPT,jsonObject);
        return transactionsManager.acceptTrade(actorId,targetId);
    }

    /**
     * 锁定交易
     * @param actorId
     * @param jsonObject
     * @return
     */
    public Result<?> lockTrade(long actorId, JSONObject jsonObject) {
        return transactionsManager.lockTrade(actorId,jsonObject);
    }


    /**
     * 确认交易
     * @param actorId
     * @param jsonObject
     * @return
     */
    public Result<?> enterTrade(long actorId, JSONObject jsonObject) {

        return transactionsManager.enterTrade(actorId,jsonObject);
    }
}
