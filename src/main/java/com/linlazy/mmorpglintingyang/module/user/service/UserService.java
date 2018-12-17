package com.linlazy.mmorpglintingyang.module.user.service;

import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserService {

    @Autowired
    private UserManager userManager;

    public Result<?> login(String username, String password, Channel channel) {

        //参数校验
        if (StringUtils.isEmpty(username)){
            return Result.valueOf("用户名不能为空");
        }

        if(StringUtils.isEmpty(password)){
            return Result.valueOf("密码不能为空");
        }

        User user = userManager.getUserByUsername(username);
        if(user == null){
            return Result.valueOf("用户名不存在");
        }



        //判断是否已登录
        if(SessionManager.isOnline(user.getActorId())){
            return Result.valueOf("已登录");
        }

        //原通道账号解绑
        if(SessionManager.isBind(channel)){
            SessionManager.unBind(channel);
        }

        //todo 加密
        if(!user.getPassword().equals(password)){
            return Result.valueOf("用户名或密码不正确");
        }


        SessionManager.bind(user.getActorId(),channel);
        return Result.success();
    }

    public Result<?> logout(Channel channel) {
        SessionManager.unBind(channel);
        return Result.success();
    }

    public Result<?> register(String username, String password, String confirmPassword) {
        //参数校验
        if (StringUtils.isEmpty(username)){
            return Result.valueOf("用户名不能为空");
        }

        if(StringUtils.isEmpty(password)){
            return Result.valueOf("密码不能为空");
        }

        if(!password.equals(confirmPassword)){
            return Result.valueOf("俩次输入密码不一致");
        }

        User user = userManager.getUserByUsername(username);
        if(user != null){
            return Result.valueOf("用户名已存在");
        }

        //注册
        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userManager.createUser(user);
        return Result.success();
    }


    /**
     * 获取玩家信息
     * @param actorId
     * @return
     */
    public  User  getUser(long actorId){
       return userManager.getUser(actorId);
    }

    public void updateUser(User user) {
        userManager.updateUser(user);
    }

}
