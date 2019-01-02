package com.linlazy.mmorpglintingyang.module.guild.domain;

import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseBackPack {

    private static Map<Integer,BaseBackPack> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(backpackType(),this);
    }

    /**
     * 背包类型
     * @return
     */
    protected abstract int backpackType();

    /**
     * 获取背包
     * @param backpackType
     * @return
     */
    public static BaseBackPack getBackPack(int backpackType){
        return map.get(backpackType);
    }

    /**
     * 整理背包
     */
    public abstract void arrangeBackPack();

    /**
     * 放进背包
     */
    public abstract void pushBackPack(Set<ItemDo> itemDoSet);

    /**
     * 移出背包
     */
    public abstract void popBackPack(Set<ItemDo> itemDoSet);

    // 1 放得下，放进背包
}
