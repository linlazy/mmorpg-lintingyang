
package com.linlazy.mmorpg.module.guild.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.dao.GuildDAO;
import com.linlazy.mmorpg.dao.GuildOffLineDAO;
import com.linlazy.mmorpg.dao.GuildPlayerDAO;
import com.linlazy.mmorpg.entity.GuildEntity;
import com.linlazy.mmorpg.entity.GuildOffLineEntity;
import com.linlazy.mmorpg.entity.GuildPlayerEntity;
import com.linlazy.mmorpg.module.backpack.dto.LatticeDTO;
import com.linlazy.mmorpg.module.backpack.service.GuildWarehouseService;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.guild.constants.GuildAuthLevel;
import com.linlazy.mmorpg.module.guild.domain.Guild;
import com.linlazy.mmorpg.module.guild.domain.GuildPlayer;
import com.linlazy.mmorpg.module.guild.domain.GuildWarehouse;
import com.linlazy.mmorpg.module.guild.dto.GuildWarehouseDTO;
import com.linlazy.mmorpg.module.guild.push.GuildPushHelper;
import com.linlazy.mmorpg.module.item.domain.ItemContext;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author linlazy
 */
@Component
public class GuildService{

    private static Map<Long,Long> actorIdGuildIdMap = new ConcurrentHashMap<>();
    private static Map<Long,Guild> guildMap = new ConcurrentHashMap<>();

    @Autowired
    private GuildPlayerDAO guildPlayerDAO;
    @Autowired
    private  GuildDAO guildDAO;
    @Autowired
    private GuildOffLineDAO guildOffLineDAO;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private  GuildWarehouseService guildWarehouseService;


    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void listenEvent(ActorEvent<JSONObject> actorEvent){
        if(actorEvent.getEventType().equals(EventType.LOGIN)){
            initGuidData();
            Long guildId = actorIdGuildIdMap.get(actorEvent.getActorId());
            if(guildId != null){
                List<GuildOffLineEntity> receiveChatSet = guildOffLineDAO.getReceiveChatSet(guildId, actorEvent.getActorId());
                receiveChatSet.stream()
                        .forEach(guildOffLineEntity -> {
                            Player player = playerService.getPlayer(guildOffLineEntity.getSenderId());
                            GuildPushHelper.pushGuild(guildOffLineEntity.getReceiverId(),String.format("玩家【%s】 申请加入公会",player.getName()));
                            guildOffLineDAO.deleteQueue(guildOffLineEntity);
                        });
            }
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
     * 申请加入公会
     * @param actorId
     * @param guildId
     * @return
     */
    public Result<?> applyJoin(long actorId,long guildId) {

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
        actorIdGuildIdMap.put(targetId,guildId);
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
        Guild guild = getGuildByActorId(actorId);
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
        initGuidData();
        Long guildId = actorIdGuildIdMap.get(actorId);
        GuildWarehouse guildWarehouse = guildWarehouseService.getGuildWarehouse(guildId);

        GuildWarehouseDTO guildWarehouseDTO =new GuildWarehouseDTO(guildId);

        if( guildWarehouse != null){
            List<LatticeDTO> latticeDTOList = Arrays.stream(guildWarehouse.getBackPack())
                    .filter(Objects::nonNull)
                    .map(LatticeDTO::new).collect(Collectors.toList());
            guildWarehouseDTO.setBackPackLatticeList(latticeDTOList);
        }
        return  Result.success(guildWarehouseDTO.toString());
    }


    /**
     * 捐献金币
     * @param actorId
     * @param gold
     * @return
     */
    public Result<?> donateGold(long actorId, long gold) {

        Player player = playerService.getPlayer(actorId);
        if(player.getGold() < gold){
            return Result.valueOf("金币不足");
        }

        Long guildId = actorIdGuildIdMap.get(actorId);
        if(guildId == null){
           return   Result.valueOf("未加入公会");
        }

        player.setGold(player.getGold() - gold);
        playerService.updatePlayer(player);

        Guild guild = guildMap.get(guildId);
        guild.setGold(guild.getGold() + gold);
        guildDAO.updateQueue(guild.convertGuildEntity());
        return Result.success();
    }



    public boolean hasGuild(long actorId) {
       initGuidData();
        return actorIdGuildIdMap.get(actorId) != null;
    }

    public boolean hasAuth(long actorId, long targetId) {
        Guild guild = getGuildByActorId(actorId);
        GuildPlayer guildPlayer = guild.getGuildPlayerMap().get(actorId);
        GuildPlayer targetGuildPlayer = guild.getGuildPlayerMap().get(targetId);
        if(guildPlayer.getAuthLevel() >= GuildAuthLevel.VICE_PRESIDENT
                && guildPlayer.getAuthLevel() > targetGuildPlayer.getAuthLevel()){
            return true;
        }
        return false;
    }

    public boolean hasAuth(long actorId,long targetId,int authLevel){
        Guild guild = getGuildByActorId(actorId);
        GuildPlayer guildPlayer = guild.getGuildPlayerMap().get(actorId);
        GuildPlayer targetGuildPlayer = guild.getGuildPlayerMap().get(targetId);
        if(guildPlayer.getAuthLevel() >= GuildAuthLevel.VICE_PRESIDENT
                && guildPlayer.getAuthLevel() > targetGuildPlayer.getAuthLevel()
                && guildPlayer.getAuthLevel() > authLevel){
            return true;
        }
        return false;
    }

    public Guild getGuildByActorId(long actorId){
        initGuidData();
        Long guildId = actorIdGuildIdMap.get(actorId);
        return guildMap.get(guildId);
    }

    private void initGuidData() {
        List<GuildPlayerEntity> allGuildPlayerList = guildPlayerDAO.getGuildPlayerList();
        Map<Long, List<GuildPlayerEntity>> guildPlayerMap = allGuildPlayerList.stream()
                .collect(groupingBy(GuildPlayerEntity::getGuildId));
        List<GuildEntity> guildList = guildDAO.getGuildList();
        guildList.stream().forEach(guildEntity -> {
            Guild guild = new Guild();

            guild.setGuildId(guildEntity.getGuildId());
            guild.setGold(guildEntity.getGold());
            guild.setLevel(guild.getLevel());

            guildPlayerMap.get(guild.getGuildId()) .forEach(guildPlayerEntity -> {
                GuildPlayer guildPlayer =new GuildPlayer(guildPlayerEntity);
                guild.getGuildPlayerMap().put(guildPlayer.getPlayer().getActorId(),guildPlayer);
                actorIdGuildIdMap.put(guildPlayer.getPlayer().getActorId(),guild.getGuildId());
            });

            GuildWarehouse guildWarehouse = guildWarehouseService.getGuildWarehouse(guild.getGuildId());
            guild.setGuildWarehouse(guildWarehouse);

            guildMap.put(guild.getGuildId(),guild);
        });
    }

    public Result<?> pop(long actorId, ArrayList<ItemContext> itemContexts) {
        initGuidData();
        Long guildId = actorIdGuildIdMap.get(actorId);

        return guildWarehouseService.pop(guildId,actorId,itemContexts);
    }

    public Result<?> push(long actorId, ArrayList<ItemContext> itemContexts) {
        initGuidData();
        Long guildId = actorIdGuildIdMap.get(actorId);

        return guildWarehouseService.push(guildId,actorId,itemContexts);
    }
}
