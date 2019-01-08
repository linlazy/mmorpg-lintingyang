package com.linlazy.mmorpglintingyang.module.user.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpglintingyang.module.common.addition.Addition;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.module.user.push.UserPushHelper;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * @author linlazy
 */
@Component
public class UserService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private GlobalConfigService globalConfigService;


    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void listenEvent(ActorEvent actorEvent){
        switch (actorEvent.getEventType()){
            case ACTOR_DAMAGE:
                handleActorDamage(actorEvent);
                break;
            default:
                break;
        }
    }

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

        if(SessionManager.getActorId(channel) != null
                && SessionManager.getActorId(channel) == user.getActorId()){
            return Result.valueOf("已登录...");
        }

        //todo 加密
        if(!user.getPassword().equals(password)){
            return Result.valueOf("用户名或密码不正确");
        }


        //同一个连接,不同账号登录
        if(SessionManager.getActorId(channel) != null &&
                SessionManager.getActorId(channel) != user.getActorId()){
            //原账号下线
            SessionManager.unBind(channel);
            //新账号上线
            SessionManager.bind(user.getActorId(),channel);
            UserPushHelper.pushLogin(user.getActorId(),"登录成功，原账号已下线");
        }

        //同一账号不同连接
        if(SessionManager.getChannel(user.getActorId()) != null &&
                !SessionManager.getChannel(user.getActorId()).equals(channel)){
            //通知原账号被迫下线
            UserPushHelper.pushLogin(user.getActorId(),"被迫下线");
            SessionManager.unBind(user.getActorId());

            //新账号上线
            SessionManager.bind(user.getActorId(),channel);
            UserPushHelper.pushLogin(user.getActorId(),"登录成功，原账号已下线");
        }

        //正常登录
        SessionManager.bind(user.getActorId(),channel);

        EventBusHolder.post(new ActorEvent<>(user.getActorId(), EventType.LOGIN));
        return Result.success("登录成功");
    }

    public Result<?> logout(Channel channel) {
        SessionManager.unBind(channel);
        return Result.success("退出成功");
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
        UserPushHelper.pushRegister(user.getActorId(),"请选择职业"+
                "输入profession 1，选择战士，高攻击，高防御+" +
                "输入profession 2，选择牧师，携带治疗技能"+
                "输入profession 3，选择法师,携带群攻技能"+
                "输入profession 4，选择召唤师，携带召唤技能");
        return Result.success("注册成功");
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

    public void addOrRemoveAddition(long actorId, Addition addition) {
        userManager.addOrRemoveAddition(actorId,addition);
    }


    /**
     * 处理玩家受到伤害事件
     * @param actorEvent
     */
    private void handleActorDamage(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject) actorEvent.getData();
        long actorId = actorEvent.getActorId();
        userManager.handleActorDamage(actorId,jsonObject);
    }

    public Result<?> selectProfession(long actorId, int professionId) {

        //todo 校验
        User user = userManager.getUser(actorId);
        if(user.getProfession() != 0){
            return Result.success("已选择职业...");
        }
        user.setProfession(professionId);
        return Result.success("选择职业成功");
    }
}
