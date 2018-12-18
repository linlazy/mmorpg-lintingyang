package com.linlazy.mmorpglintingyang.utils;

public class ItemIdUtil {

    /**
     * 获取策划配置ID
     * @param itemId
     * @return
     */
    public static int getBaseItemId(long itemId){
        //取低28位表示baseItemId
        return (int) (itemId & 0xfffffff);
    }

    /**
     * 获取策划配置ID
     * @param baseItemIdOrderId
     * @return
     */
    public static int getBaseItemId(String baseItemIdOrderId){
        return Integer.parseInt(baseItemIdOrderId.split(":")[0]);
    }

    /**
     * 自增序号，背包索引，策划配置ID组成唯一ID
     * @param orderId
     * @param backPackIndex
     * @param baseItemId
     * @return
     */
    public static long getNewItemId(long orderId, int backPackIndex, int baseItemId) {
        long i = (orderId << 40) + (backPackIndex << 28) + baseItemId;
        return i;
    }

    /**
     * 自增序号
     * @param itemId
     * @return
     */
    public static int getOrderId(long itemId) {
        //取高24位做orderId
        return (int) ((itemId >>40) &0xffffff);
    }

    /**
     * 背包索引
     * @param itemId
     * @return
     */
    public static int getBackPackIndex(long itemId) {
        //取中12位做backPackIndex
        return (int) ((itemId >> 28) & 0xfff);
    }

    /**
     * 获取策划配置自增序号key
     * @param itemId
     * @return
     */
    public static String getBaseItemIdOrderIdKey(long itemId) {
        return getBaseItemId(itemId) + ":" + getOrderId(itemId);
    }

    /**
     * 获取自增序号
     * @param baseItemIdOrderId
     * @return
     */
    public static int getOrderId(String baseItemIdOrderId) {
        return Integer.parseInt(baseItemIdOrderId.split(":")[1]);
    }
}
