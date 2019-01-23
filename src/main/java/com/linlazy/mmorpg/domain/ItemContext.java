package com.linlazy.mmorpg.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class ItemContext {


    private long itemId;

    private int count;

    private boolean superPosition;

    private int superPositionUp;
    private int itemType;

    public ItemContext(long itemId) {
        this.itemId = itemId;
        initConfig(ItemIdUtil.getBaseItemId(this.itemId));
    }


    public void initConfig(int baseItemId){
        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        Integer superPositionUp = itemConfig.getInteger("superPosition");
        if(superPositionUp == null){
            this.setSuperPositionUp(1);
            this.setSuperPosition(false);
        }else {
            this.setSuperPosition(true);
            this.setSuperPositionUp(superPositionUp);
        }

        this.setItemType(itemConfig.getIntValue("itemType"));
    }
}
