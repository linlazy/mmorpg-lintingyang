package com.linlazy.mmorpglintingyang.module.item.manager.dao;

import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Set;

@Mapper
public interface ItemDao {

    @Select("select * from item where actorId = #{actorId},itemId = #{itemId}")
    Item getItem(long actorId,int item);

    @Update("update item set count = #{count} where actorId = #{actorId},itemId = #{itemId}")
    void updateItem(Item item);


    @Select("select * from item where actorId = #{actorId}")
    Set<Item> getItemSet(long actorId);
}
