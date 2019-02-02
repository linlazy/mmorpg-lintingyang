package com.linlazy.mmorpg.module.backpack;

import com.linlazy.mmorpg.module.backpack.domain.Lattice;
import com.linlazy.mmorpg.module.item.domain.Item;

import java.util.List;


/**
 * 背包接口
 * @author linlazy
 */
public interface BackpackInterface {


    /**
     * 获取背包
     * @return
     */
    Lattice[] getBackPack();

    /**
     * 放进背包
     * @param itemList 要放进背包的道具
     * @return 返回结果
     */
    boolean push(List<Item> itemList);

    /**
     * 是否已满
     * @param itemList 要放进背包的道具
     * @return 放回结果
     */
    boolean isFull(List<Item> itemList);

    /**
     * 是否足够
     * @param itemList 要取走的道具
     * @return 返回结果
     */
    boolean isNotEnough(List<Item> itemList);

    /**
     * 消耗道具，取出背包
     * @param itemContextList
     * @return 放回取出结果
     */
    boolean pop(List<Item> itemContextList);

    /**
     * 整理背包
     * @return 返回整理后
     */
    Lattice[] arrangeBackPack();

}
