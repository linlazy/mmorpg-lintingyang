package com.linlazy.mmorpglintingyang.module.pk.arena.manager;

import com.linlazy.mmorpglintingyang.module.pk.arena.entity.Arena;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author linlazy
 */
@Mapper
public interface ArenaDao {

    /**
     * 获取玩家竞技场信息
     * @param arenaId  竞技场ID
     * @param actorId 玩家ID
     * @return 放回玩家竞技场信息
     */
    @Select("select * from arena where arenaId = #{arenaId} and actorId = #{actorId}")
    Arena getArena(int arenaId, int actorId);

    /**
     * 更新玩家竞技场信息
     * @param arena 竞技场信息
     */
    @Update({"update arena set score = #{score},killNum = #{killNum},killedNum = #{killedNum}" ,
            " where arenaId = #{arenaId} and actorId = #{actorId}"})
    void updateArena(Arena arena);

    /**
     * 增加竞技场信息
     * @param  area
     */
    @Insert({"insert into arena(arenaId,actorId,killNum,killedNum,score)",
            "values(#{arenaId},#{actorId},#{killNum},#{killedNum},#{score})"})
    void addArena(Arena area);
}
