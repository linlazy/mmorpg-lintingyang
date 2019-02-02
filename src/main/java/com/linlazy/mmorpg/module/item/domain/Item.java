package com.linlazy.mmorpg.module.item.domain;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.entity.GuildWarehouseEntity;
import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.file.service.ItemConfigService;
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
     * 道具标识
     */
    private long id;

    /**
     * 道具ID
     */
    private long itemId;

    /**
     * 道具类型
     */
    private int itemType;

    /**
     * 道具名称
     */
    private String name;

    /**
     * 扩展配置属性
     */
    private JSONObject extConfig;
    /**
     * 数量
     */
    private int count;

    private JSONObject ext;

    private JSONObject itemConfig;

    public Item(long itemId) {
        initConfig(ItemIdUtil.getBaseItemId(itemId));
    }

    public boolean isSuperPosition(){
        return itemConfig.getIntValue("superPosition") != 0;
    }

    public int getSuperPositionUp(){
        return itemConfig.getIntValue("superPosition");
    }



    public int finalAttack(){
        int attack = itemConfig.getIntValue("attack");
        int level = ext.getIntValue("level");

        return attack + level * 6;
    }

    public int finalDefense(){
        int defense = itemConfig.getIntValue("defense");
        int level = ext.getIntValue("level");

        return defense + level * 6;
    }

    public void modifyDurability() {
        int durability = ext.getIntValue("durability");
        durability--;
        if(durability <= 0){
            durability = 0;
        }
        ext.put("durability",durability);
    }


    public Item(long itemId,int count) {
        this.itemId = itemId;
        this.count = count;
        initConfig(ItemIdUtil.getBaseItemId(itemId));
    }

    public Item(ItemEntity itemEntity) {
        this.itemId = itemEntity.getItemId();
        this.count = itemEntity.getCount();
        this.ext =itemEntity.getExtJsonObject();
        initConfig(ItemIdUtil.getBaseItemId(itemId));
    }

    public Item(GuildWarehouseEntity guildWarehouseEntity) {
        this.itemId = guildWarehouseEntity.getItemId();
        this.count = guildWarehouseEntity.getCount();
        this.ext =guildWarehouseEntity.getExtJsonObject();
        initConfig(ItemIdUtil.getBaseItemId(itemId));
    }


    public Item clonez(){
        return new Item(itemId,count);
    }

    public ItemEntity convertItemEntity(){
        ItemEntity item = new ItemEntity();

        item.setItemId(itemId);
        item.setCount(count);
        if(ext != null){
            item.setExtJsonObject(ext);
        }

        return item;
    }




    /**
     * 初始化配置数据
     */
    protected void initConfig(int baseItemId) {
        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        this.itemConfig = itemConfigService.getItemConfig(baseItemId);
        name = itemConfig.getString("name");
        itemType = itemConfig.getIntValue("itemType");
        extConfig =itemConfig.getJSONObject("ext");
    }
}
