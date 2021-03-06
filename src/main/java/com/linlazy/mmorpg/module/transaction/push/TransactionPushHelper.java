package com.linlazy.mmorpg.module.transaction.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.PushHelper;

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

    public static void pushTransaction(long actorId,String message){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", message);
        PushHelper.push(actorId,jsonObject);
    }
}
