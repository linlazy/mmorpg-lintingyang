package com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import lombok.Data;

import java.util.Objects;
/**
 *
 * 道具领域类
 */
@Data
public class ItemDo{


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
     * 扩展属性
     */
    private String ext;

    public void setItemId(long itemId){
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

    public ItemDo(Item item) {
    }

    ItemDo clonez(){
        ItemDo itemDo = new ItemDo();
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
        long itemId = (orderId << 40) + (backPackIndex << 28) + baseItemId;
        item.setItemId(itemId);
        item.setActorId(this.actorId);
        item.setCount(this.count);
        item.setExt(ext);
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDo itemDo = (ItemDo) o;
        return getBaseItemIdOrderIdKey() == itemDo.getBaseItemIdOrderIdKey();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBaseItemIdOrderIdKey());
    }
}
