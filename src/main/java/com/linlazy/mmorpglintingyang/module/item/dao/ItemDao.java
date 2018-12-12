package com.linlazy.mmorpglintingyang.module.item.dao;

import com.linlazy.mmorpglintingyang.module.item.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ItemDao {

    @Select("select * from item where actorId = #{actorId},itemId = #{itemId}")
    Item getItem(long actorId,int item);

    @Update("update item set count = #{count} where actorId = #{actorId},itemId = #{itemId}")
    void updateItem(Item item);
}
