package com.linlazy.mmorpg.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.constants.TransactionOperatiorType;
import com.linlazy.mmorpg.domain.ItemContext;
import com.linlazy.mmorpg.domain.PlayerBackpack;
import com.linlazy.mmorpg.domain.Transaction;
import com.linlazy.mmorpg.push.TransactionPushHelper;
import com.linlazy.mmorpg.server.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author linlazy
 */
@Component
public class TransactionService {

    private static Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private AtomicInteger maxTransactionId = new AtomicInteger(0);

    @Autowired
    private PlayerBackpackService playerBackpackService;

    private Map<Integer, Transaction> transactionIdMap = new ConcurrentHashMap<>();
    private  Map<Long,Integer> actorIdTransactionIdMap = new ConcurrentHashMap<>();



    private Transaction createTransactionDo(long acceptor, long inviter, int transactionId) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setInviter(inviter);
        transaction.setAcceptor(acceptor);
        return transaction;
    }
     /**
     * 邀请交易
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> inviteTrade(long actorId, long targetId) {
        if(isTrading(actorId,targetId)){
            return Result.valueOf("玩家已处于交易状态");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourceId",actorId);
        TransactionPushHelper.pushTransactionOperation(targetId, TransactionOperatiorType.INVITE,jsonObject);
        return Result.success();
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
        int transactionId = maxTransactionId.incrementAndGet();

        Transaction transaction = createTransactionDo(actorId,targetId,transactionId);
        transactionIdMap.put(transactionId, transaction);
        actorIdTransactionIdMap.put(actorId,transactionId);
        actorIdTransactionIdMap.put(targetId,transactionId);
        logger.debug("transactionInfo:{}", transaction);
        return Result.success();
    }

    /**
     * 锁定交易
     * @param actorId
     * @param jsonObject
     * @return
     */
    public Result<?> lockTrade(long actorId, JSONObject jsonObject) {
        int transactionId = actorIdTransactionIdMap.get(actorId);
        Transaction transaction = transactionIdMap.get(transactionId);
        if( transaction.getInviter() == actorId){
            //邀请者锁定
            transaction.setInviterLock(true);

            List<ItemContext> itemContextList = new ArrayList<>();
            JSONArray items = jsonObject.getJSONArray("items");
            for(int i=0; i < items.size() ; i++){
                JSONObject item = items.getJSONObject(i);
                long itemId = item.getLongValue("itemId");
                int count = item.getIntValue("num");
                ItemContext itemContext = new ItemContext(itemId);
                itemContext.setCount(count);
                itemContextList.add(itemContext);
            }
            transaction.setInviterItemContextList(itemContextList);
        }else {
            //接受者锁定
            transaction.setAcceptorLock(true);
            List<ItemContext> itemList = new ArrayList<>();
            JSONArray items = jsonObject.getJSONArray("items");
            for(int i=0; i < items.size() ; i++){
                JSONObject item = items.getJSONObject(i);
                long itemId = item.getLongValue("itemId");
                int count = item.getIntValue("num");
                ItemContext itemContext = new ItemContext(itemId);
                itemContext.setCount(count);
                itemList.add(itemContext);
            }
            transaction.setAcceptorItemContextList(itemList);
        }

        logger.debug("transactionInfo:{}", transaction);
        return Result.success();
    }


    /**
     * 确认交易
     * @param actorId
     * @param jsonObject
     * @return
     */
    public Result<?> enterTrade(long actorId, JSONObject jsonObject) {

        int transactionId = actorIdTransactionIdMap.get(actorId);
        Transaction transaction = transactionIdMap.get(transactionId);
        if( transaction.getInviter() == actorId){
            //邀请者确认交易
            transaction.setInviterEnter(true);
            logger.debug("inviterEnter:{}",actorId);
        }else {
            //接受者确认交易
            transaction.setAcceptorEnter(true);
            logger.debug("acceptorEnter:{}",actorId);
        }

        if(transaction.isInviterEnter()&& transaction.isAcceptorEnter()){
            //执行交易
            logger.debug("执行交易:{}", transaction);

            long inviter = transaction.getInviter();
            long acceptor = transaction.getAcceptor();
            List<ItemContext> inviterItemContextList = transaction.getInviterItemContextList();
            List<ItemContext> acceptorItemContextList = transaction.getAcceptorItemContextList();

            PlayerBackpack inviterPlayerBackpack = playerBackpackService.getPlayerBackpack(inviter);
            PlayerBackpack acceptPlayerBackpack = playerBackpackService.getPlayerBackpack(acceptor);
            try{
                inviterPlayerBackpack.getReadWriteLock().writeLock().lock();
                acceptPlayerBackpack.getReadWriteLock().writeLock().lock();

                inviterPlayerBackpack.pop(inviterItemContextList);
                acceptPlayerBackpack.push(inviterItemContextList);

                playerBackpackService.getPlayerBackpack(acceptor).pop(acceptorItemContextList);
                playerBackpackService.getPlayerBackpack(inviter).push(acceptorItemContextList);
            }finally {
                inviterPlayerBackpack.getReadWriteLock().writeLock().unlock();
                acceptPlayerBackpack.getReadWriteLock().writeLock().unlock();
            }

            transactionIdMap.remove(transactionId);
            actorIdTransactionIdMap.remove(transaction.getInviter());
            actorIdTransactionIdMap.remove(transaction.getAcceptor());
        }

        return Result.success();
    }


    /**
     * 玩家是否处于正在交易状态
     * @param actorId
     * @param targetId
     * @return
     */
    public boolean isTrading(long actorId,long targetId){
        return actorIdTransactionIdMap.get(actorId) != null || actorIdTransactionIdMap.get(targetId) != null;
    }

    public boolean isLock(long actorId) {
        return  actorIdTransactionIdMap.get(actorId) != null;
    }


}
