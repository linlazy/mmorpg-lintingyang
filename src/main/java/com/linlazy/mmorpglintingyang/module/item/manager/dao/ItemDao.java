package com.linlazy.mmorpglintingyang.module.item.manager.dao;

import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import org.apache.ibatis.annotations.*;

import java.util.Set;

/**
 * @author linlazy
 */
@Mapper
public interface ItemDao {

    /**
     * 获取道具信息
     * @param actorId 玩家ID
     * @param itemId 道具ID
     * @return 返回道具信息
     */
    @Select("select * from item where actorId = #{actorId} and itemId = #{itemId}")
    Item getItem(long actorId,long itemId);

    /**
     * 更新道具信息
     * @param item 道具信息
     */
    @Update("update item set count = #{count},ext = #{ext} where actorId = #{actorId} and itemId = #{itemId}")
    void updateItem(Item item);


    /**
     * 获取玩家道具集合
     * @param actorId 玩家ID
     * @return 返回玩家道具集合
     */
    @Select("select * from item where actorId = #{actorId}")
    Set<Item> getItemSet(long actorId);

    /**
     * 清空玩家道具
     * @param actorId 玩家ID
     */
    @Delete("delete from item where actorId = #{actorId}")
    void deleteActorItems(long actorId);

    /**
     * 增加道具
     * @param item 道具信息
     */
    @Insert("insert into item(actorId,itemId,count,ext)values(#{actorId},#{itemId},#{count},#{ext})")
    void addItem(Item item);

    /**
     * 删除道具
     * @param item 道具信息
     */
    @Delete("delete from item where actorId = #{actorId} and itemId = #{itemId}")
    void deleteItem(Item item);
}
