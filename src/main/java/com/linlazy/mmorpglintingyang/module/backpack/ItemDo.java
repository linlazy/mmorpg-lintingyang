package com.linlazy.mmorpglintingyang.module.backpack;

import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import lombok.Data;

import java.util.Objects;
/**
 *
 * 道具领域类
 */
@Data
public class ItemDo{

    /**
     * 策划配置Id +order +baseItemId
     */
    private long id;

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
    private  boolean superPosition;

    /**
     * 扩展属性
     */
    private String ext;

    public ItemDo() {
    }

    public ItemDo(Item item) {
    }

    ItemDo clonez(){
        ItemDo itemDo = new ItemDo();

        itemDo.setId(this.id);
        itemDo.setCount(count);
        itemDo.setSuperPosition(this.superPosition);
        itemDo.setSuperPositionUp(this.superPositionUp);

        return itemDo;
    }

     long getBaseItemIdOrderIdKey(){
        //高24位 低28位
        return ((id >>40) &0xffffff) + (id & 0xfffffff);
    }

    public Item convertItem(){
         Item item = new Item();
        item.setItemId(this.id);
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
