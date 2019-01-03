package com.linlazy.mmorpglintingyang.module.transaction.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.transaction.service.TransactionService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Module("transaction")
@Component
public class TransactionHandler {

    @Autowired
    private TransactionService transactionService;

    /**
     * 邀请交易
     * @param jsonObject
     * @return
     */
    @Cmd("inviteTrade")
    public Result<?> inviteTrade(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return transactionService.inviteTrade(actorId,targetId);
    }

    /**
     * 拒绝交易
     * @param jsonObject
     * @return
     */
    @Cmd("rejectTrade")
    public Result<?> rejectTrade(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return transactionService.rejectTrade(actorId,targetId);
    }

    /**
     * 同意交易
     * @param jsonObject
     * @return
     */
    @Cmd("acceptTrade")
    public Result<?> acceptTrade(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return transactionService.acceptTrade(actorId,targetId);
    }

    /**
     * 锁定交易
     * @param jsonObject
     * @return
     */
    @Cmd("lockTrade")
    public Result<?> lockTrade(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return transactionService.lockTrade(actorId,jsonObject);
    }

    /**
     * 确认交易
     * @param jsonObject
     * @return
     */
    @Cmd("enterTrade")
    public Result<?> enterTrade(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return transactionService.enterTrade(actorId,jsonObject);
    }

}
