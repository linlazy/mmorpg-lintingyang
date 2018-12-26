package com.linlazy.mmorpglintingyang.module.pk.arena.manager;

import com.linlazy.mmorpglintingyang.module.pk.arena.entity.Arena;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ArenaDao {

    @Select("select * from arena where arenaId = #{arenaId} and actorId = #{killId}")
    Arena getArena(int arenaId, int killId);

    @Update("update arena set score = #{score} where arenaId = #{arenaId} and actorId = #{killId}")
    void updateArena(Arena convertArena);
}
