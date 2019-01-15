package com.linlazy.mmorpg.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class ItemContext {

    private int baseItemId;

    private long itemId;

    private int count;

    private boolean superPosition;

    private int superPositionUp;


    public void setBaseItemId(int baseItemId) {
        this.baseItemId = baseItemId;
        this.initConfig(baseItemId);
    }

    public void initConfig(int baseItemId){
        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        Integer superPositionUp = itemConfig.getInteger("superPosition");
        if(superPositionUp == null){
            superPositionUp =1;
            this.setSuperPosition(false);
        }else {
            this.setSuperPosition(true);
        }
        this.setSuperPositionUp(superPositionUp);
    }
}