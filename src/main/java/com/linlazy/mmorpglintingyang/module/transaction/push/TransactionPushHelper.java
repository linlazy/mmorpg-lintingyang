package com.linlazy.mmorpglintingyang.module.transaction.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.PushHelper;

/**
 * 交易推送类
 * @author linlazy
 */
public class TransactionPushHelper {

    public static void pushTransactionOperation(long actorId, int operatorType, JSONObject jsonObject){
        jsonObject.put("push","transaction");
        jsonObject.put("operation",operatorType);
        PushHelper.push(actorId,jsonObject);
    }
}
