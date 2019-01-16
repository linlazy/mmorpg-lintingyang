package com.linlazy.mmorpg.domain;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.entity.GuildWarehouseEntity;
import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.SpringContextUtil;
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

    private String ext;


    public Item() {
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

    public Item(int baseItemId) {
        this.itemId =baseItemId;
        initConfig(baseItemId);
    }


    public Item clonez(){
        Item item = new Item();

        item.setItemId(itemId);
        item.setCount(count);
        item.setSuperPosition(this.superPosition);
        item.setSuperPositionUp(this.superPositionUp);

        return item;
    }

    public ItemEntity convertItemEntity(){
        ItemEntity item = new ItemEntity();

        item.setItemId(itemId);
        item.setCount(count);
        item.setExt(ext);

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
