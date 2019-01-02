package com.linlazy.mmorpglintingyang.module.transaction.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.backpack.manager.BackPackManager;
import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import com.linlazy.mmorpglintingyang.module.transaction.constants.TransactionOperatiorType;
import com.linlazy.mmorpglintingyang.module.transaction.domain.TransactionDo;
import com.linlazy.mmorpglintingyang.module.transaction.push.TransactionPushHelper;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TransactionsManager {
    private static Logger logger = LoggerFactory.getLogger(TransactionsManager.class);

    private Map<Integer, TransactionDo> transactionIdMap = new ConcurrentHashMap<>();
    private  Map<Long,Integer> actorIdTransactionIdMap = new ConcurrentHashMap<>();

    private AtomicInteger maxTransactionId = new AtomicInteger(0);

    @Autowired
    private ItemManager itemManager;
    @Autowired
    private BackPackManager backPackManager;

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
        logger.debug("transactionInfo:{}",transactionDo);
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
                itemDoSet.add(itemDo);
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
                itemDoSet.add(itemDo);
            }
            transactionDo.setAcceptorItemDoSet(itemDoSet);
        }

        logger.debug("transactionInfo:{}",transactionDo);
        return Result.success();
    }

    public Result<?> enterTrade(long actorId, JSONObject jsonObject) {
        int transactionId = actorIdTransactionIdMap.get(actorId);
        TransactionDo transactionDo = transactionIdMap.get(transactionId);
        if( transactionDo.getInviter() == actorId){
            //邀请者确认交易
            transactionDo.setInviterEnter(true);
            logger.debug("inviterEnter:{}",actorId);
        }else {
            //接受者确认交易
            transactionDo.setAcceptorEnter(true);
            logger.debug("acceptorEnter:{}",actorId);
        }

        if(transactionDo.isInviterEnter()&&transactionDo.isAcceptorEnter()){
            //执行交易
            logger.debug("执行交易:{}",transactionDo);

            long inviter = transactionDo.getInviter();
            long acceptor = transactionDo.getAcceptor();
            Set<ItemDo> inviterItemDoSet = transactionDo.getInviterItemDoSet();
            Set<ItemDo> acceptorItemDoSet = transactionDo.getAcceptorItemDoSet();
            inviterItemDoSet.stream()
                    .forEach(itemDo -> {
                        itemManager.consumeBackPackItem(inviter,itemDo.getItemId(),itemDo.getCount());
                        int baseItemId = ItemIdUtil.getBaseItemId(itemDo.getItemId());
                        itemManager.pushBackPack(acceptor,baseItemId,itemDo.getCount());
                    });
            acceptorItemDoSet.stream()
                    .forEach(itemDo -> {
                        itemManager.consumeBackPackItem(acceptor,itemDo.getItemId(),itemDo.getCount());
                        int baseItemId = ItemIdUtil.getBaseItemId(itemDo.getItemId());
                        itemManager.pushBackPack(inviter,baseItemId,itemDo.getCount());
                    });

            transactionIdMap.remove(transactionId);
            actorIdTransactionIdMap.remove(transactionDo.getInviter());
            actorIdTransactionIdMap.remove(transactionDo.getAcceptor());
        }

        return Result.success();
    }

    public boolean isLock(long actorId) {
        return  actorIdTransactionIdMap.get(actorId) != null;
    }
}
