package com.linlazy.mmorpglintingyang.module.domain;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.entity.GuildWarehouseEntity;
import com.linlazy.mmorpglintingyang.module.entity.ItemEntity;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import lombok.Data;

/**
 * 道具领域类
 * @author linlazy
 */
@Data
public class Item {

    /**
     * 道具ID
     */
    private long itemId;

    /**
     * 可折叠
     */
    private boolean superPosition;

    /**
     * 折叠上限
     */
    private int superPositionUp;

    /**
     * 数量
     */
    private int count;

    private Item() {
    }


    public Item(long itemId,int count) {
        this.itemId = itemId;
        this.count = count;
        initConfig(ItemIdUtil.getBaseItemId(itemId));
    }

    public Item(ItemEntity itemEntity) {
        this.itemId = itemEntity.getItemId();
        this.superPosition = itemEntity.isSuperPosition();
        this.superPositionUp = itemEntity.getSuperPositionUp();
        this.count = itemEntity.getCount();
    }

    public Item(GuildWarehouseEntity guildWarehouseEntity) {
        this.itemId = guildWarehouseEntity.getItemId();
        this.superPosition = guildWarehouseEntity.isSuperPosition();
        this.superPositionUp = guildWarehouseEntity.getSuperPositionUp();
        this.count = guildWarehouseEntity.getCount();
    }

    public Item clonez(){
        Item item = new Item();

        item.setItemId(itemId);
        item.setCount(count);
        item.setSuperPosition(this.superPosition);
        item.setSuperPositionUp(this.superPositionUp);

        return item;
    }


    /**
     * 初始化配置数据
     */
    private void initConfig(int baseItemId) {
        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        this.superPositionUp = itemConfig.getIntValue("superPositionUp");
        this.superPosition = itemConfig.getBooleanValue("superPosition");
    }
}
