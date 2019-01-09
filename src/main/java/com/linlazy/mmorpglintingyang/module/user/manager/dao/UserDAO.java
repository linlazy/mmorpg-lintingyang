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
public interface UserDAO {

    String TABLE = "user";


    /**
     * 获取用户信息
     * @param actorId  玩家ID
     * @return 返回用户信息
     */
    @Select({"select * from ",TABLE," where actorId = #{actorId} limit 1"})
    User getUser(long actorId);


    /**
     *  通过用户名获取用户信息
     * @param username 用户名
     * @return 返回用户信息
     */
    @Select({"select * from ",TABLE," where username = #{username} limit 1"})
    User getUserByUsername(String username);

    /**
     * 创建用户
     * @param user 用户信息
     * @return 返回创建结果
     */
    @Insert({"insert into ",TABLE,"(token,username,password,sceneId)values(#{token},#{username},#{password},#{sceneId})"})
    boolean createUser(User user);

    /**
     * 获取玩家最大ID
     * @return 返回最大玩家ID
     */
    @Select({"select max(actorId) from ",TABLE})
    Long getMaxActorId();

    /**
     * 更新用户信息
     * @param user 用户信息
     */
    @Update({"update ",TABLE,"set sceneId = #{sceneId}, mp = #{mp},mpNextResumeTime = #{mpNextResumeTime},hp = #{hp}",
                "where actorId = #{actorId}"})
    void updateUser(User user);

}
