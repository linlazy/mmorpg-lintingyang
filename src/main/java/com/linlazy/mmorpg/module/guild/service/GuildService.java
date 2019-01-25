
package com.linlazy.mmorpg.module.guild.service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.module.guild.manager.GuildManager;
//import com.linlazy.mmorpg.module.guild.validator.GuildValidator;
//import com.linlazy.mmorpg.server.common.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.dao.GuildDAO;
import com.linlazy.mmorpg.dao.GuildOffLineDAO;
import com.linlazy.mmorpg.dao.GuildPlayerDAO;
import com.linlazy.mmorpg.entity.GuildEntity;
import com.linlazy.mmorpg.entity.GuildOffLineEntity;
import com.linlazy.mmorpg.entity.GuildPlayerEntity;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.guild.constants.GuildAuthLevel;
import com.linlazy.mmorpg.module.guild.domain.Guild;
import com.linlazy.mmorpg.module.guild.domain.GuildPlayer;
import com.linlazy.mmorpg.module.guild.push.GuildPushHelper;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linlazy
 */
@Component
public class GuildService {

    private Map<Long,Long> actorIdGuildIdMap = new ConcurrentHashMap<>();
    private Map<Long,Guild> guildMap = new ConcurrentHashMap<>();

    @Autowired
    private GuildPlayerDAO guildPlayerDAO;
    @Autowired
    private GuildDAO guildDAO;
    @Autowired
    private GuildOffLineDAO guildOffLineDAO;
    @Autowired
    private PlayerService playerService;

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void listenEvent(ActorEvent<JSONObject> actorEvent){
        if(actorEvent.getEventType().equals(EventType.LOGIN)){
            Long guildId = actorIdGuildIdMap.get(actorEvent.getActorId());
            List<GuildOffLineEntity> receiveChatSet = guildOffLineDAO.getReceiveChatSet(guildId, actorEvent.getActorId());
            receiveChatSet.stream()
                    .forEach(guildOffLineEntity -> {
                        Player player = playerService.getPlayer(guildOffLineEntity.getSenderId());
                        GuildPushHelper.pushGuild(guildOffLineEntity.getReceiverId(),String.format("玩家【%s】 申请加入公会",player.getName()));
                        guildOffLineDAO.deleteQueue(guildOffLineEntity);
                    });

        }

    }


    /**
     * 创建公会
     * @param actorId
     * @return
     */
    public Result<?> createGuild(long actorId) {
        if(hasGuild(actorId)){
            return Result.valueOf("已加入公会");
        }

        long maxGuildId = guildDAO.getMaxGuildId().incrementAndGet();
        GuildPlayerEntity guildPlayerEntity = new GuildPlayerEntity();
        guildPlayerEntity.setActorId(actorId);
        guildPlayerEntity.setGuildId(maxGuildId);
        guildPlayerEntity.setAuthLevel(GuildAuthLevel.PRESIDENT);
        guildPlayerDAO.insertQueue(guildPlayerEntity);
        actorIdGuildIdMap.put(actorId,maxGuildId);


        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setGuildId(maxGuildId);
        guildEntity.setGold(0);
        guildEntity.setLevel(1);
        guildDAO.insertQueue(guildEntity);

        return Result.success();
    }


    /**
     * 加入公会
     * @param actorId
     * @param guildId
     * @return
     */
    public Result<?> applyJoin(long actorId, long guildId) {

        if(hasGuild(actorId)){
            return Result.valueOf("已加入公会");
        }
        Guild guild = guildMap.get(guildId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("senderId",actorId);
        Set<GuildPlayer> hasAuthInvitePlayerSet = guild.getHasAuthInvitePlayerSet();
        hasAuthInvitePlayerSet.stream()
            .forEach(guildPlayer -> {
                //在线则直接推送
                if(SessionManager.isOnline(guildPlayer.getPlayer().getActorId())){
                    Player player = playerService.getPlayer(actorId);
                    GuildPushHelper.pushGuild(guildPlayer.getPlayer().getActorId(),String.format("玩家【%s】 申请加入公会",player.getName()));
                }else {
                    //不在线则，先存档，下次登录时推送
                    GuildOffLineEntity guildOffLine = new GuildOffLineEntity();
                    guildOffLine.setGuildId(guildId);
                    guildOffLine.setReceiverId(guildPlayer.getPlayer().getActorId());
                    guildOffLine.setSenderId(actorId);
                    guildOffLineDAO.insertQueue(guildOffLine);
                }
            });
        return Result.success();
    }

    /**
     * 同意加入公会
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> acceptJoin(long actorId, long targetId) {
        Long guildId = actorIdGuildIdMap.get(actorId);
        actorIdGuildIdMap.put(actorId,guildId);
        GuildPlayerEntity guildPlayerEntity = new GuildPlayerEntity();
        guildPlayerEntity.setActorId(targetId);
        guildPlayerEntity.setGuildId(guildId);
        guildPlayerEntity.setAuthLevel(GuildAuthLevel.NEW_MEMBER);


        GuildPlayer guildPlayer = new GuildPlayer(guildPlayerEntity);
        Guild guild = guildMap.get(guildId);
        guild.getGuildPlayerMap().put(guildPlayer.getGuildId(),guildPlayer);

        guildPlayerDAO.insertQueue(guildPlayerEntity);
        return Result.success();
    }

    /**
     * 任命
     * @param actorId
     * @param targetId
     * @param authLevel
     * @return
     */
    public Result<?> appoint(long actorId, long targetId, int authLevel) {
        if(!hasAuth(actorId,targetId,authLevel)){
            return Result.valueOf("无权限");
        }
        Long guildId = actorIdGuildIdMap.get(actorId);
        Guild guild = guildMap.get(guildId);
        GuildPlayer guildPlayer = guild.getGuildPlayerMap().get(targetId);
        guildPlayer.setAuthLevel(authLevel);
        guildPlayerDAO.updateQueue(guildPlayer.convertGuildPlayerEntity());
        return Result.success();
    }


    /**
     * 踢出公会
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> shotOffGuild(long actorId, long targetId) {
        if(!hasAuth(actorId,targetId)){
            return Result.valueOf("无权限");
        }

        Long guildId = actorIdGuildIdMap.remove(targetId);
        GuildPlayer guildPlayer = guildMap.get(guildId).getGuildPlayerMap().remove(targetId);
        guildPlayerDAO.deleteQueue(guildPlayer.convertGuildPlayerEntity());
        return Result.success();
    }

    /**
     * 获取公会仓库信息
     * @param actorId
     * @return
     */
    public Result<?> guildWareHouseInfo(long actorId) {

        return null;
    }


    /**
     * 捐献金币
     * @param actorId
     * @param gold
     * @return
     */
    public Result<?> donateGold(long actorId, int gold) {

        Long guildId = actorIdGuildIdMap.get(actorId);
        Guild guild = guildMap.get(guildId);
        guild.setGold(guild.getGold() + gold);
        guildDAO.updateQueue(guild.convertGuildEntity());
        return Result.success();
    }



    public boolean hasGuild(long actorId) {
        return actorIdGuildIdMap.get(actorId) != null;
    }

    public boolean hasAuth(long actorId, long targetId) {
        Long guildId = actorIdGuildIdMap.get(actorId);
        Guild guild = guildMap.get(guildId);
        GuildPlayer guildPlayer = guild.getGuildPlayerMap().get(actorId);
        GuildPlayer targetGuildPlayer = guild.getGuildPlayerMap().get(targetId);
        if(guildPlayer.getAuthLevel() >= GuildAuthLevel.VICE_PRESIDENT
                && guildPlayer.getAuthLevel() > targetGuildPlayer.getAuthLevel()){
            return true;
        }
        return false;
    }

    public boolean hasAuth(long actorId,long targetId,int authLevel){
        Long guildId = actorIdGuildIdMap.get(actorId);
        Guild guild = guildMap.get(guildId);
        GuildPlayer guildPlayer = guild.getGuildPlayerMap().get(actorId);
        GuildPlayer targetGuildPlayer = guild.getGuildPlayerMap().get(targetId);
        if(guildPlayer.getAuthLevel() >= GuildAuthLevel.VICE_PRESIDENT
                && guildPlayer.getAuthLevel() > targetGuildPlayer.getAuthLevel()
                && guildPlayer.getAuthLevel() > authLevel){
            return true;
        }
        return false;
    }
}
