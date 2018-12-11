package com.linlazy.mmorpglintingyang.module.user.dao;

import com.linlazy.mmorpglintingyang.module.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from user where actorId = #{actorId} limit 1")
    User getUser(long actorId);

    @Select("select * from user where username = #{username} limit 1")
    User getUserByUsername(String username);

    @Insert("insert into user(token,username,password)values(#{token},#{username},#{password})")
    boolean createUser(User user);

    @Select({"select max(actorId) from user"})
    Long getMaxActorId();
}
