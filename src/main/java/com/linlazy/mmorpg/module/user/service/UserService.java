package com.linlazy.mmorpg.module.user.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.dao.PlayerDAO;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.entity.PlayerEntity;
import com.linlazy.mmorpg.module.common.addition.Addition;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.user.manager.entity.User;
import com.linlazy.mmorpg.module.user.push.UserPushHelper;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.SessionManager;
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
    private PlayerDAO playerDAO;
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

        PlayerEntity playerEntity = playerDAO.getUserByUsername(username);
        if(playerEntity == null){
            return Result.valueOf("用户名不存在");
        }

        if(SessionManager.getActorId(channel) != null
                && SessionManager.getActorId(channel) == playerEntity.getActorId()){
            return Result.valueOf("已登录...");
        }

        //todo 加密
        if(!playerEntity.getPassword().equals(password)){
            return Result.valueOf("用户名或密码不正确");
        }


        //同一个连接,不同账号登录
        if(SessionManager.getActorId(channel) != null &&
                SessionManager.getActorId(channel) != playerEntity.getActorId()){
            //原账号下线
            SessionManager.unBind(channel);
            //新账号上线
            SessionManager.bind(playerEntity.getActorId(),channel);
            UserPushHelper.pushLogin(playerEntity.getActorId(),"登录成功，原账号已下线");
        }

        //同一账号不同连接
        if(SessionManager.getChannel(playerEntity.getActorId()) != null &&
                !SessionManager.getChannel(playerEntity.getActorId()).equals(channel)){
            //通知原账号被迫下线
            UserPushHelper.pushLogin(playerEntity.getActorId(),"被迫下线");
            SessionManager.unBind(playerEntity.getActorId());

            //新账号上线
            SessionManager.bind(playerEntity.getActorId(),channel);
            UserPushHelper.pushLogin(playerEntity.getActorId(),"登录成功，原账号已下线");
        }

        //正常登录
        SessionManager.bind(playerEntity.getActorId(),channel);

        EventBusHolder.post(new ActorEvent<>(playerEntity.getActorId(), EventType.LOGIN));
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

        PlayerEntity playerEntity = playerDAO.getUserByUsername(username);
        if(playerEntity != null){
            return Result.valueOf("用户名已存在");
        }

        //注册
        playerEntity = new PlayerEntity();
        playerEntity.setUsername(username);
        playerEntity.setPassword(password);
        playerDAO.insertQueue(playerEntity);

        UserPushHelper.pushRegister(playerEntity.getActorId(),"请选择职业"+
                "输入profession 1，选择战士，高攻击，高防御+" +
                "输入profession 2，选择牧师，携带治疗技能"+
                "输入profession 3，选择法师,携带群攻技能"+
                "输入profession 4，选择召唤师，携带召唤技能");
        return Result.success("注册成功");
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
        PlayerEntity playerEntity = playerDAO.getEntityByPK(actorId);
        if(playerEntity.getProfession() != 0){
            return Result.success("已选择职业...");
        }
        playerEntity.setProfession(professionId);
        playerDAO.updateQueue(playerEntity);
        return Result.success("选择职业成功");
    }
}
