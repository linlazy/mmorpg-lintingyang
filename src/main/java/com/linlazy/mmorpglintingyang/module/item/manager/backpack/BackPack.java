package com.linlazy.mmorpglintingyang.module.item.manager.backpack;

import com.linlazy.mmorpglintingyang.module.common.GlobalConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class BackPack {

    /**
     * 首次初始化背包时构建
     */
    private Map<Integer,Long> baseItemIdOrderIdMap = new HashMap<>();

    /**
     * 背包
     */
    private Item[] items;

    @Autowired
    private GlobalConfigService globalConfigService;


    @PostConstruct
    public void init(){
        int packageMaxLatticeNum = globalConfigService.getPackageMaxLatticeNum();
        items = new Item[packageMaxLatticeNum];
    }
}
