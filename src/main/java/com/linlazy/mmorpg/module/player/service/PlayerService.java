package com.linlazy.mmorpg.module.player.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpg.dao.PlayerDAO;
import com.linlazy.mmorpg.entity.PlayerEntity;
import com.linlazy.mmorpg.event.type.LoginEvent;
import com.linlazy.mmorpg.event.type.PlayerAttackEvent;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.dto.PlayerDTO;
import com.linlazy.mmorpg.module.player.push.PlayerPushHelper;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.module.skill.service.SkillService;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.server.common.LogoutListener;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.SessionManager;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 玩家服务类
 * @author linlazy
 */
@Component
public class PlayerService {

    private static Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private PlayerDAO playerDAO;

    @Autowired
    private SkillService skillService;

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 注册同步锁
     */
    private byte[] registerSynLock = new byte[0];

    /**
     * 登录同步锁
     */
    private byte[] loginSynLock = new byte[0];

    /**
     * 玩家缓存
     */
    public static LoadingCache<Long, Player> playerCache = CacheBuilder.newBuilder()
            .recordStats()
            .build(new CacheLoader<Long, Player>() {
                @Override
                public Player load(Long actorId) {


                    PlayerDAO playerDAO = SpringContextUtil.getApplicationContext().getBean(PlayerDAO.class);
                    PlayerEntity playerEntity = playerDAO.getEntityByPK(actorId);
                    Player player = new Player(playerEntity);

                    return player;
                }
            });


    /**
     * 起服时构建set，供注册查询使用
     */
    private Set<String> set = new CopyOnWriteArraySet<>();
    @PostConstruct
    public void init(){
        List<String> allPlayerUsername = playerDAO.selectAllPlayerUsername();
        allPlayerUsername
                .forEach(username->{
                    set.add(username);
                });
    }


    public Player getPlayer(long actorId){
        try {
            return playerCache.get(actorId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Long,Player> getSameScenePlayerMap(int sceneId){
        Map<Long,Player> result = new HashMap<>();

       playerCache.asMap().values().stream()
               .filter(player -> player.getSceneId() == sceneId)
               .filter(player -> SessionManager.isOnline(player.getActorId()))
               .forEach(player -> {
                   result.put(player.getActorId(),player);
               });

       return result;
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

        if(!playerEntity.getPassword().equals(password)){
            return Result.valueOf("用户名或密码不正确");
        }

        //登录同步锁
        synchronized (loginSynLock){

            //同一个连接,不同账号登录
            if(SessionManager.getActorId(channel) != null &&
                    SessionManager.getActorId(channel) != playerEntity.getActorId()){
                //原账号下线
                SessionManager.unBind(channel);
                //新账号上线
                SessionManager.bind(playerEntity.getActorId(),channel);
                PlayerPushHelper.pushLogin(playerEntity.getActorId(),"登录成功，原账号已下线");
            }

            //同一账号不同连接

            if(SessionManager.getChannel(playerEntity.getActorId()) != null &&
                    !SessionManager.getChannel(playerEntity.getActorId()).equals(channel)){
                //通知原账号被迫下线
                PlayerPushHelper.pushLogin(playerEntity.getActorId(),"被迫下线");
                SessionManager.unBind(playerEntity.getActorId());
                SessionManager.bind(playerEntity.getActorId(),channel);
                PlayerPushHelper.pushLogin(playerEntity.getActorId(),"登录成功，原账号已下线");
            }
            //正常登录
            SessionManager.bind(playerEntity.getActorId(),channel);
        }


        EventBusHolder.post(new ActorEvent<>(playerEntity.getActorId(), EventType.LOGIN));
        EventBusHolder.post(new LoginEvent(getPlayer(playerEntity.getActorId())));

        if(playerEntity.getProfession() == 0 ){
            PlayerPushHelper.pushRegister(playerEntity.getActorId(),"请选择职业\n"+
                    "输入profession 1，选择战士，高攻击，高防御\n"+
                    "输入profession 2，选择牧师，携带治疗技能\n"+
                    "输入profession 3，选择法师,携带群攻技能\n"+
                    "输入profession 4，选择召唤师，携带召唤技能\n");
        }else {
            Player player = getPlayer(playerEntity.getActorId());
            PlayerPushHelper.pushPlayerInfo(player.getActorId(),new PlayerDTO(player));
        }
        return Result.success("登录成功");
    }

    public Result<?> logout(Channel channel) {
        Long actorId = SessionManager.getActorId(channel);
        if(actorId != null){
            SpringContextUtil.getApplicationContext().getBeansOfType(LogoutListener.class).values()
                    .stream().forEach(logoutListener -> logoutListener.logout(actorId));

            SessionManager.unBind(channel);
        }

        return Result.success("退出成功");
    }

    public Result<?> register(String username, String password, String confirmPassword, Channel channel) {
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
        PlayerEntity playerEntity = null;
        synchronized (registerSynLock){

            if(set.contains(username)){
                return Result.valueOf("用户名已存在");
            }

            //注册
        System.out.println(Thread.currentThread().getName());

            playerEntity = new PlayerEntity();
            AtomicLong maxActorId = playerDAO.getMaxActorId();
            playerEntity.setActorId(maxActorId.incrementAndGet());
            playerEntity.setUsername(username);
            playerEntity.setPassword(password);
            playerEntity.setLevel(globalConfigService.getInitLevel());
            playerEntity.setHp(globalConfigService.getInitHp());
            playerEntity.setSceneId(globalConfigService.getInitScene());
            playerDAO.insertQueue(playerEntity);
            Player player = new Player(playerEntity);
            player.setMaxHP(globalConfigService.getInitHp());
            playerCache.put(player.getActorId(),player);
            set.add(username);
        }
            SessionManager.bind(playerEntity.getActorId(),channel);
            PlayerPushHelper.pushRegister(playerEntity.getActorId(),"请选择职业\n"+
                    "输入profession 1，选择战士，高攻击，高防御\n"+
                    "输入profession 2，选择牧师，携带治疗技能\n"+
                    "输入profession 3，选择法师,携带群攻技能\n"+
                    "输入profession 4，选择召唤师，携带召唤技能\n");

        EventBusHolder.post(new ActorEvent<>(playerEntity.getActorId(),EventType.REGISTER));
        return Result.success("注册成功");
    }


    public Result<?> selectProfession(long actorId, int professionId) {

        Player player = getPlayer(actorId);
        if(player.getProfession() != 0){
            return Result.success("已选择职业...");
        }
        player.setProfession(professionId);
        playerDAO.updateQueue(player.convertPlayerEntity());

        skillService.initPlayerProfessionSkill(player);

        PlayerPushHelper.pushPlayerInfo(player.getActorId(),new PlayerDTO(player));
        return Result.success("选择职业成功");
    }

    public Result<?> attack(long actorId, int skillId) {
        Player player = getPlayer(actorId);
        if(!player.hasSkill(skillId)){
           return Result.valueOf("玩家未拥有该技能");
        }



        Skill skill = player.getPlayerSkill().getSkillMap().get(skillId);


        int mp = skill.getSkillTemplateArgs().getIntValue("mp");
        if(player.getMp() < mp){
            return Result.valueOf("MP不足");
        }
        if(DateUtils.getNowMillis() <  skill.getNextCDResumeTimes()){
            return Result.valueOf("技能冷卻中");
        }

        skillService.useSkill(player,skill);
        EventBusHolder.post(new PlayerAttackEvent(player));
        return Result.success();
    }


    public Result<?> upgradeLevel(long actorId) {
        Player player = getPlayer(actorId);
        long maxExp = player.getMaxExp();
        player.addExp(maxExp);
        PlayerPushHelper.pushChange(actorId,new PlayerDTO(player));
        playerDAO.updateQueue(player.convertPlayerEntity());
        EventBusHolder.post(new ActorEvent<JSONObject>(actorId,EventType.ACTOR_LEVEL_UP));
        return Result.success();
    }

    public void updatePlayer(Player player) {
        playerDAO.updateQueue(player.convertPlayerEntity());
    }

    public Result<?> gainGold(long actorId, long addGold) {
        Player player = getPlayer(actorId);
        player.setGold(player.getGold() + addGold);
        playerDAO.updateQueue(player.convertPlayerEntity());
        EventBusHolder.post(new ActorEvent<>(actorId,EventType.ACTOR_GOLD_CHANGE));
        return Result.success();
    }

    public Result<?> addExp(long actorId, long exp) {
        Player player = getPlayer(actorId);
        player.addExp(exp);
        updatePlayer(player);
        return Result.success();
    }
}
