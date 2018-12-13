package com.linlazy.mmorpglintingyang.module.skill.manager.dao;

import com.linlazy.mmorpglintingyang.module.skill.manager.entity.Skill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SkillDao {


    @Select("select * from skill where actorId = #{actorId} limit 1")
    Skill getSkill(long actorId);

    @Insert("insert into skill (actorId,skills)values(#{actorId},#{skills})")
    boolean addSkill(Skill skill);

    @Update("update skill set skills = #{skills} where actorId = #{actorId}")
     boolean updateSkill(Skill skill);
}
