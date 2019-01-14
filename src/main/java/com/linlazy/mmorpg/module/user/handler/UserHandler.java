package com.linlazy.mmorpg.module.user.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.module.user.service.UserService;
import com.linlazy.mmorpg.server.route.Cmd;
import com.linlazy.mmorpg.server.route.Module;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
@Module("user")
public class UserHandler {


    @Autowired
    private UserService userService;

    /**
     * 注册
     * @param jsonObject
     * @return
     */
    @Cmd(value ="register",auth =false)
    public Result<?> register(JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String confirmPassword = jsonObject.getString("confirm_password");
        return userService.register(username,password,confirmPassword);
    }

    /**
     * 登入
     * @param jsonObject
     * @return
     */
    @Cmd(value ="login",auth = false)
    public Result<?> login(JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        Channel channel = jsonObject.getObject("channel",Channel.class);
        return userService.login(username,password,channel);
    }

    /**
     * 选择职业
     * @param jsonObject
     * @return
     */
    @Cmd(value ="selectProfession")
    public Result<?> selectProfession(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        int professionId = jsonObject.getIntValue("actorId");
        return userService.selectProfession(actorId,professionId);
    }

    /**
     * 登出
     * @param jsonObject
     * @return
     */
    @Cmd(value = "logout")
    public Result<?> logout(JSONObject jsonObject){
        Channel channel = jsonObject.getObject("channel",Channel.class);
        return userService.logout(channel);
    }

//    /**
//     * 获取玩家信息
//     * @param jsonObject
//     * @return
//     */
//    @Cmd(value = "getInfo")
//    public Result<?> getInfo(JSONObject jsonObject){
//        long actorId = jsonObject.getLongValue("actorId");
//        return Result.success(userService.getUser(actorId));
//    }
}
