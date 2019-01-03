package com.linlazy.mmorpglintingyang.module.skill.dao;

import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Set;

/**
 * @author linlazy
 */
@Mapper
public interface SkillDao {


    String INSERT_FIELD = " actorId,skillId,level,dressed,nextCDResumeTimes ";

    @Select("select * from skill where actorId = #{actorId} and skillId = #{skillId}")
    Skill getSkill(long actorId,int skillId);

    @Select("select * from skill where actorId = #{actorId}")
    Set<Skill> getSkillSet(long actorId);

    @Insert({"insert into skill (",INSERT_FIELD,")" +
            "values(#{actorId},#{skillId},#{level},#{dressed},#{nextCDResumeTimes})"})
    boolean addSkill(Skill skill);

    @Update({"update skill",
            " set level = #{level} , dressed = #{dressed} , nextCDResumeTimes = #{nextCDResumeTimes} ",
            "where actorId = #{actorId} and skillId = #{skillId}"})
     boolean updateSkill(Skill skill);
}
