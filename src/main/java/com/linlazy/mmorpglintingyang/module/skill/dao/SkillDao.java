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

    /**
     * 获取玩家技能信息
     * @param actorId  玩家ID
     * @param skillId 技能ID
     * @return 返回玩家技能ID
     */
    @Select("select * from skill where actorId = #{actorId} and skillId = #{skillId}")
    Skill getSkill(long actorId,int skillId);


    /**
     * 获取玩家所有技能信息
     * @param actorId  玩家ID
     * @return 返回玩家所有技能信息
     */
    @Select("select * from skill where actorId = #{actorId}")
    Set<Skill> getSkillSet(long actorId);

    /**
     * 增加技能
     * @param skill 技能信息
     * @return 返回增加技能结果
     */
    @Insert({"insert into skill (",INSERT_FIELD,")" +
            "values(#{actorId},#{skillId},#{level},#{dressed},#{nextCDResumeTimes})"})
    boolean addSkill(Skill skill);

    /**
     * 更新技能
     * @param skill 技能信息
     * @return 返回更新结果
     */
    @Update({"update skill",
            " set level = #{level} , dressed = #{dressed} , nextCDResumeTimes = #{nextCDResumeTimes} ",
            "where actorId = #{actorId} and skillId = #{skillId}"})
     boolean updateSkill(Skill skill);
}
