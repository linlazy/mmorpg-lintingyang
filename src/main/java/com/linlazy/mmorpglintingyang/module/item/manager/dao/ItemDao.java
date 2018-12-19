package com.linlazy.mmorpglintingyang.module.item.manager.dao;

import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface ItemDao {

    @Select("select * from itemDo where actorId = #{actorId} and itemId = #{itemId}")
    Item getItem(long actorId,long itemId);

    @Update("update itemDo set count = #{count},ext = #{ext} where actorId = #{actorId} and itemId = #{itemId}")
    void updateItem(Item item);


    @Select("select * from itemDo where actorId = #{actorId}")
    Set<Item> getItemSet(long actorId);

    @Delete("delete from itemDo where actorId = #{actorId}")
    void deleteActorItems(long actorId);

    @Insert("insert into itemDo(actorId,itemId,count,ext)values(#{actorId},#{itemId},#{count},#{ext})")
    void addItem(Item item);
    @Delete("delete from itemDo where actorId = #{actorId} and itemId = #{itemId}")
    void deleteItem(Item item);
}
