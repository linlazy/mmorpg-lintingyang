package com.linlazy.mmorpglintingyang.module.user.manager.dao;

import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author linlazy
 */
@Mapper
public interface UserDao {

    String TABLE = "user";


    @Select({"select * from ",TABLE," where actorId = #{actorId} limit 1"})
    User getUser(long actorId);

    @Select({"select * from ",TABLE," where username = #{username} limit 1"})
    User getUserByUsername(String username);

    @Insert({"insert into ",TABLE,"(token,username,password,sceneId)values(#{token},#{username},#{password},#{sceneId})"})
    boolean createUser(User user);

    @Select({"select max(actorId) from ",TABLE})
    Long getMaxActorId();

    @Update({"update ",TABLE,"set sceneId = #{sceneId}, mp = #{mp},mpNextResumeTime = #{mpNextResumeTime},hp = #{hp}",
                "where actorId = #{actorId}"})
    void updateUser(User user);

}
