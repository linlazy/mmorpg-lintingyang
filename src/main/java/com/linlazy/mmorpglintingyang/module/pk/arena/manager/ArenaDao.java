package com.linlazy.mmorpglintingyang.module.pk.arena.manager;

import com.linlazy.mmorpglintingyang.module.pk.arena.entity.Arena;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ArenaDao {

    @Select("select * from arena where arenaId = #{arenaId} and actorId = #{killId}")
    Arena getArena(int arenaId, int killId);

    @Update({"update arena set score = #{score},killNum = #{killNum},killedNum = #{killedNum}" ,
            " where arenaId = #{arenaId} and actorId = #{actorId}"})
    void updateArena(Arena arena);

    @Insert({"insert into arena(arenaId,actorId,killNum,killedNum,score)",
            "values(#{arenaId},#{actorId},#{killNum},#{killedNum},#{score})"})
    void addArena(Arena killArena);
}
