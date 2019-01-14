//package com.linlazy.mmorpg.module.item.service.strategy;
//
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.server.common.Result;
//import com.linlazy.mmorpg.utils.SpringContextUtil;
//
///**
// * @author linlazy
// */
//public abstract class BaseUseItemStrategy {
//    public static BaseUseItemStrategy newUseItemStrategy(int useItem) {
//
//        switch (useItem){
//            case 1:
//                return SpringContextUtil.getApplicationContext().getBean(UseItemStrategy0.class);
//            default:
//                return SpringContextUtil.getApplicationContext().getBean(UseItemStrategy0.class);
//        }
//    }
//
//    /**
//     *  执行使用道具
//     * @param actorId 玩家ID
//     * @param itemId 道具ID
//     * @param jsonObject 可变参数
//     * @return 返回使用道具结果
//     */
//    public abstract Result<?> doUseItem(long actorId, long itemId, JSONObject jsonObject);
//}
