package com.linlazy.mmorpglintingyang.module.transaction.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import com.linlazy.mmorpglintingyang.module.transaction.constants.TransactionOperatiorType;
import com.linlazy.mmorpglintingyang.module.transaction.domain.TransactionDo;
import com.linlazy.mmorpglintingyang.module.transaction.push.TransactionPushHelper;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TransactionManager {


    private Map<Integer, TransactionDo> transactionIdMap = new ConcurrentHashMap<>();
    private  Map<Long,Integer> actorIdTransactionIdMap = new ConcurrentHashMap<>();

    private AtomicInteger maxTransactionId = new AtomicInteger(0);

    /**
     * 处于交易状态
     * @param actorId
     * @return
     */
    public boolean isTrading(long actorId) {
        return actorIdTransactionIdMap.get(actorId) != null;
    }

    public Result<?> inviteTrade(long actorId, long targetId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourceId",actorId);
        TransactionPushHelper.pushTransactionOperation(targetId, TransactionOperatiorType.INVITE,jsonObject);
        return Result.success();
    }

    public Result<?> acceptTrade(long actorId, long targetId) {
        int transactionId = maxTransactionId.incrementAndGet();

        TransactionDo transactionDo = createTransactionDo(actorId,targetId,transactionId);
        transactionIdMap.put(transactionId,transactionDo);
        actorIdTransactionIdMap.put(actorId,transactionId);
        actorIdTransactionIdMap.put(targetId,transactionId);
        return Result.success();
    }

    private TransactionDo createTransactionDo(long acceptor, long inviter, int transactionId) {
        TransactionDo transactionDo = new TransactionDo();
        transactionDo.setTransactionId(transactionId);
        transactionDo.setInviter(inviter);
        transactionDo.setAcceptor(acceptor);
        return transactionDo;
    }

    public Result<?> lockTrade(long actorId, JSONObject jsonObject) {
        int transactionId = actorIdTransactionIdMap.get(actorId);
        TransactionDo transactionDo = transactionIdMap.get(transactionId);
        if( transactionDo.getInviter() == actorId){
            //邀请者锁定
            transactionDo.setInviterLock(true);

            Set<ItemDo> itemDoSet = new HashSet<>();
            JSONArray items = jsonObject.getJSONArray("items");
            for(int i=0; i < items.size() ; i++){
                JSONObject item = items.getJSONObject(i);
                long itemId = item.getLongValue("itemId");
                int count = item.getIntValue("num");
                ItemDo itemDo = new ItemDo(itemId);
                itemDo.setCount(count);
                itemDo.setActorId(actorId);
            }
            transactionDo.setInviterItemDoSet(itemDoSet);
        }else {
            //接受者锁定
            transactionDo.setAcceptorLock(true);
            Set<ItemDo> itemDoSet = new HashSet<>();
            JSONArray items = jsonObject.getJSONArray("items");
            for(int i=0; i < items.size() ; i++){
                JSONObject item = items.getJSONObject(i);
                long itemId = item.getLongValue("itemId");
                int count = item.getIntValue("num");
                ItemDo itemDo = new ItemDo(itemId);
                itemDo.setCount(count);
                itemDo.setActorId(actorId);
            }
            transactionDo.setAcceptorItemDoSet(itemDoSet);
        }

        return Result.success();
    }

    public Result<?> enterTrade(long actorId, JSONObject jsonObject) {
        int transactionId = actorIdTransactionIdMap.get(actorId);
        TransactionDo transactionDo = transactionIdMap.get(transactionId);
        if( transactionDo.getInviter() == actorId){
            //邀请者确认交易
            transactionDo.setInviterEnter(true);
        }else {
            //接受者确认交易
            transactionDo.setAcceptorEnter(true);
        }

        if(transactionDo.isInviterEnter()&&transactionDo.isAcceptorEnter()){
            //执行交易
        }
        return Result.success();
    }
}
