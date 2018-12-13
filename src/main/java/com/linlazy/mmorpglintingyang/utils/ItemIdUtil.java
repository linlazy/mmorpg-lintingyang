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
     * 自增序号，背包索引，策划配置ID组成唯一ID
     * @param order
     * @param index
     * @param baseItemId
     * @return
     */
    public static long getNewItemId(int order, int index, int baseItemId) {
        return 0;
    }
}
