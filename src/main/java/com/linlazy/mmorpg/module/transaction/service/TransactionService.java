package com.linlazy.mmorpg.module.transaction.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.domain.PlayerBackpack;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.module.transaction.constants.TransactionOperatiorType;
import com.linlazy.mmorpg.module.transaction.domain.Transaction;
import com.linlazy.mmorpg.module.transaction.push.TransactionPushHelper;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    @Autowired
    private PlayerService playerService;

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
        if( !SessionManager.isOnline(targetId)){
            return Result.valueOf("玩家不在线");
        }

        Player player = playerService.getPlayer(actorId);
        Player targetPlayer = playerService.getPlayer(targetId);

        TransactionPushHelper.pushTransaction(targetId, String.format("玩家【%s】邀请您交易...",player.getName()));
        return Result.success();
    }

    public Result<?> rejectTrade(long actorId, long targetId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("senderId",actorId);
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
        Player player = playerService.getPlayer(actorId);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("senderId",actorId);
        TransactionPushHelper.pushTransactionOperation(targetId, TransactionOperatiorType.ACCEPT,jsonObject);
        int transactionId = maxTransactionId.incrementAndGet();

        Transaction transaction = createTransactionDo(actorId,targetId,transactionId);
        transactionIdMap.put(transactionId, transaction);
        actorIdTransactionIdMap.put(actorId,transactionId);
        actorIdTransactionIdMap.put(targetId,transactionId);
        logger.debug("transactionInfo:{}", transaction);
        TransactionPushHelper.pushTransaction(targetId, String.format("玩家【%s】接受了您的交易...",player.getName()));
        return Result.success();
    }

    /**
     * 锁定交易
     * @param actorId
     * @param jsonObject
     * @return
     */
    public Result<?> lockTrade(long actorId, JSONObject jsonObject) {
        Integer transactionId = actorIdTransactionIdMap.get(actorId);
        if(transactionId == null){
            return Result.valueOf("参数错误");
        }


        long itemId = jsonObject.getLongValue("itemId");
        int count = jsonObject.getIntValue("num");
        if(itemId == 0){
            return Result.valueOf("交易已锁定");
        }
        Item item = new Item(itemId,count);

        Transaction transaction = transactionIdMap.get(transactionId);
        if( transaction.getInviter() == actorId){
            //邀请者锁定
            Player player = playerService.getPlayer(transaction.getInviter());
            player.lockBackpack(true);
            transaction.setInviterItemList(Lists.newArrayList(item));
        }else {
            //接受者锁定
            Player player = playerService.getPlayer(transaction.getAcceptor());
            player.lockBackpack(true);
            transaction.setAcceptorItemList(Lists.newArrayList(item));
        }

        logger.debug("transactionInfo:{}", transaction);
        return Result.success("交易已锁定");
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
            List<Item> inviterItemList = transaction.getInviterItemList();
            List<Item> acceptorItemList = transaction.getAcceptorItemList();



            PlayerBackpack inviterPlayerBackpack = playerBackpackService.getPlayerBackpack(inviter);
            Result<?> inviterEnough = playerBackpackService.isEnough(inviter, inviterItemList);
            if(inviterEnough.isFail()){
                return Result.valueOf(inviterEnough.getCode());
            }
            Result<?> inviterNotFull = playerBackpackService.isNotFull(inviter, acceptorItemList);
            if(inviterNotFull.isFail()){
                return Result.valueOf(inviterNotFull.getCode());
            }



            PlayerBackpack acceptPlayerBackpack = playerBackpackService.getPlayerBackpack(acceptor);
            Result<?> acceptorEnough = playerBackpackService.isEnough(acceptor, acceptorItemList);
            if(acceptorEnough.isFail()){
                return Result.valueOf(acceptorEnough.getCode());
            }

            Result<?> acceptorNotFull = playerBackpackService.isNotFull(acceptor, inviterItemList);
            if(acceptorNotFull.isFail()){
                return Result.valueOf(acceptorNotFull.getCode());
            }

            //校验通过后正式执行
            try{
                inviterPlayerBackpack.getReadWriteLock().writeLock().lock();
                acceptPlayerBackpack.getReadWriteLock().writeLock().lock();

                inviterPlayerBackpack.pop(inviterItemList);
                inviterPlayerBackpack.push(acceptorItemList);
                acceptPlayerBackpack.pop(acceptorItemList);
                acceptPlayerBackpack.push(inviterItemList);

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


    public void clear(long actorId) {
        Integer transactionId = actorIdTransactionIdMap.remove(actorId);

        Transaction transaction = transactionIdMap.remove(transactionId);
        actorIdTransactionIdMap.remove(transaction.getInviter());
        actorIdTransactionIdMap.remove(transaction.getAcceptor());

    }
}
