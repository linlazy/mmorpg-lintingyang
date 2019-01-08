package com.linlazy.mmorpglintingyang.module.backpack;

import com.linlazy.mmorpglintingyang.module.backpack.domain.ItemContext;
import com.linlazy.mmorpglintingyang.module.domain.Lattice;

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
     * 获得道具
     */
    boolean addItem(List<ItemContext> itemList);

    /**
     * 消耗道具
     */
    boolean consumeItem(List<ItemContext> itemContextList);

    /**
     * 整理背包
     */
    Lattice[] arrangeBackPack();

    /**
     * 移动道具
     */
    boolean moveItem();
}
