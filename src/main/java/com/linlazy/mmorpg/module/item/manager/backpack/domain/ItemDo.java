package com.linlazy.mmorpg.module.item.manager.backpack.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.guild.entity.GuildWarehouse;
import com.linlazy.mmorpg.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpg.module.item.manager.entity.Item;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

import java.util.Objects;
/**
 *
 * 道具领域类
 * @author linlazy
 */
@Data
public class ItemDo{

    /**
     * 标识
     */
    private long itemId;
    /**
     * 背包索引
     */
    private int backPackIndex;
    /**
     * 自增序列
     */
    private int orderId;

    /**
     * 策划配置ID
     */
    private int baseItemId;

    /**
     * 玩家ID
     */
    private long actorId;
    /**
     * 道具数量
     */
    private int count;

    /**
     * 可折叠上限
     */
    private  int superPositionUp;

    /**
     * 是否可折叠
     */
    private  boolean superPosition = true;

    /**
     * 道具类型
     */
    private int itemType;

    /**
     * 扩展属性
     */
    private String ext;

    public void setItemId(long itemId){
        this.itemId =itemId;
        this.setBaseItemId((int) (itemId & 0xfffffff));
        this.setBackPackIndex((int) ((itemId >> 28) & 0xfff));
        this.setOrderId((int) ((itemId >>40) &0xffffff));
    }

    public void setBaseItemId(int baseItemId) {
        this.baseItemId = baseItemId;
        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        Integer superPositionUp = itemConfig.getInteger("superPosition");
        if(superPositionUp == null){
            superPositionUp =1;
            this.setSuperPosition(false);
        }
        this.setSuperPositionUp(superPositionUp);
    }

    public ItemDo() {
    }

    public ItemDo(long itemId) {
        this.itemId = itemId;
        this.baseItemId = ItemIdUtil.getBaseItemId(itemId);
        initConfig(baseItemId);
    }

    public ItemDo(int baseItemId) {
        this.baseItemId =baseItemId;
        initConfig(baseItemId);
    }

    /**
     * 初始化配置数据
     */
    private void initConfig(int baseItemId) {
        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        this.itemType = itemConfig.getIntValue("itemType");
    }
    public ItemDo(Item item) {
        this.itemId = item.getItemId();
        this.baseItemId = ItemIdUtil.getBaseItemId(itemId);
        this.backPackIndex = ItemIdUtil.getOrderId(itemId);
        this.orderId = ItemIdUtil.getOrderId(itemId);

        this.actorId = item.getActorId();
        this.count = item.getCount();
        this.ext = item.getExt();
        initConfig(baseItemId);
    }

    public ItemDo clonez(){
        ItemDo itemDo = new ItemDo();
        itemDo.setItemId(itemId);
        itemDo.setActorId(actorId);
        itemDo.setOrderId(orderId);
        itemDo.setBackPackIndex(backPackIndex);
        itemDo.setBaseItemId(baseItemId);
        itemDo.setCount(count);
        itemDo.setSuperPosition(this.superPosition);
        itemDo.setSuperPositionUp(this.superPositionUp);

        return itemDo;
    }


    public String getBaseItemIdOrderIdKey(){
        //高24位 低28位
        return this.orderId +":"+ this.baseItemId;
    }

    public Item convertItem(){
         Item item = new Item();
        item.setItemId(this.itemId);
        item.setActorId(this.actorId);
        item.setCount(this.count);
        item.setExt(ext);
        return item;
    }

    public GuildWarehouse convertGuildWarehouse(){
        GuildWarehouse guildWarehouse = new GuildWarehouse();
        return guildWarehouse;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemDo itemDo = (ItemDo) o;
        return getBaseItemIdOrderIdKey().equals(itemDo.getBaseItemIdOrderIdKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBaseItemIdOrderIdKey());
    }

}
