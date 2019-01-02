package com.linlazy.mmorpglintingyang.module.guild.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.guild.constants.GuildAuthLevel;
import com.linlazy.mmorpglintingyang.module.guild.constants.GuildOpertorType;
import com.linlazy.mmorpglintingyang.module.guild.dao.GuildActorDao;
import com.linlazy.mmorpglintingyang.module.guild.dao.GuildDao;
import com.linlazy.mmorpglintingyang.module.guild.dao.GuildOffLineDao;
import com.linlazy.mmorpglintingyang.module.guild.dao.GuildWarehouseDao;
import com.linlazy.mmorpglintingyang.module.guild.domain.GuildDo;
import com.linlazy.mmorpglintingyang.module.guild.entity.Guild;
import com.linlazy.mmorpglintingyang.module.guild.entity.GuildActor;
import com.linlazy.mmorpglintingyang.module.guild.entity.GuildOffLine;
import com.linlazy.mmorpglintingyang.module.guild.entity.GuildWarehouse;
import com.linlazy.mmorpglintingyang.module.guild.push.GuildPushHelper;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class GuildManager {


    @Autowired
    private GuildActorDao guildActorDao;
    @Autowired
    private GuildOffLineDao guildOffLineDao;
    @Autowired
    private GuildDao guildDao;
    @Autowired
    private GuildWarehouseDao guildWarehouseDao;

    private Map<Long,Long> actorIdGuildIdMap = new ConcurrentHashMap<>();

    private AtomicLong maxGuildId = new AtomicLong(0);

    public boolean hasGuild(long actorId) {
        return actorIdGuildIdMap.get(actorId) != null;
    }

    /**
     * 创建公会
     * @param actorId
     * @return
     */
    public Result<?> createGuild(long actorId) {

        long newGuildId = maxGuildId.incrementAndGet();
        GuildActor guildActor = new GuildActor();
        guildActor.setActorId(actorId);
        guildActor.setGuildId(newGuildId);
        guildActor.setAuthLevel(GuildAuthLevel.President);
        guildActorDao.addGuild(guildActor);
        actorIdGuildIdMap.put(actorId,newGuildId);
        return Result.success();
    }


    /**
     * 加入公会
     * @param actorId
     * @param guildId
     * @return
     */
    public Result<?> applyJoin(long actorId, long guildId) {

        GuildDo guildDo = getGuildDo(guildId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourceId",actorId);
        Set<Long> hasAuthInviteSet = guildDo.getHasAuthInviteSet();
        for(Long targetId:hasAuthInviteSet){
            //在线则直接推送
            if(SessionManager.isOnline(targetId)){
                GuildPushHelper.pushGuildOperator(targetId, GuildOpertorType.ApplyJoin,jsonObject);
            }else {
                //不在线则，先存档，下次登录时推送
                GuildOffLine guildOffLine = new GuildOffLine();
                guildOffLine.setGuild(guildId);
                guildOffLine.setReceiver(targetId);
                guildOffLine.setSourceId(actorId);
                guildOffLine.setType(GuildOpertorType.ApplyJoin);
                guildOffLineDao.addGuildOffLine(guildOffLine);
            }
        }
        return Result.success();
    }


    public GuildDo getGuildDo(long guildId) {
        GuildDo guildDo = new GuildDo();
        guildDo.setGuildId(guildId);

        Set<GuildActor> guildActorSet = guildActorDao.getGuildSet(guildId);
        for(GuildActor guildActor : guildActorSet){
            switch (guildActor.getAuthLevel()){
                case GuildAuthLevel.President:
                    guildDo.setPresidentId(guildActor.getActorId());
                break;
                case GuildAuthLevel.VicePresident:
                    guildDo.getVoidPresidentIdSet().add(guildActor.getActorId());
                break;
                case GuildAuthLevel.ExcellentMember:
                    guildDo.getExcellentMemberIdSet().add(guildActor.getActorId());
                    break;
                case GuildAuthLevel.OrdinaryMember:
                    guildDo.getOrdinaryMemberIdSet().add(guildActor.getActorId());
                    break;
                default:
                    guildDo.getNewMemberIdSet().add(guildActor.getActorId());
            }
        }

        return guildDo;
    }

    public Result<?> acceptJoin(long actorId, long targetId) {
        Long guildId = actorIdGuildIdMap.get(actorId);
        actorIdGuildIdMap.put(actorId,guildId);
        GuildActor guildActor = new GuildActor();
        guildActor.setActorId(targetId);
        guildActor.setGuildId(guildId);
        guildActor.setAuthLevel(GuildAuthLevel.NewMember);
        guildActorDao.addGuild(guildActor);
        return Result.success();
    }

    public Result<?> appoint(long actorId, long targetId, int authLevel) {
        Long guildId = actorIdGuildIdMap.get(actorId);
        GuildActor guildActor = guildActorDao.getGuild(guildId, targetId);
        guildActor.setAuthLevel(authLevel);
        guildActorDao.updateGuild(guildActor);
        return Result.success();
    }

    public long getGuildId(long actorId) {
        return actorIdGuildIdMap.get(actorId);
    }

    /**
     * 踢出公会
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> shotOffGuild(long actorId, long targetId) {
        Long guildId = actorIdGuildIdMap.remove(targetId);
        GuildActor guildActor = guildActorDao.getGuild(guildId, targetId);
        guildActorDao.deleteGuild(guildActor);
        return Result.success();
    }


    /**
     * 捐献金币
     * @param actorId
     * @param gold
     * @return
     */
    public Result<?> donateGold(long actorId, int gold) {

        Long guildId = actorIdGuildIdMap.get(actorId);
        Guild guild = guildDao.getGuildGold(guildId);
        guild.setGold(guild.getGold() + gold);
        guildDao.updateGuildGold(guild);
        return Result.success();
    }

    public Result<?> getGuildWareHouse(long actorId) {
        Long guildId = actorIdGuildIdMap.get(actorId);
        Set<GuildWarehouse> guildWarehouse = guildWarehouseDao.getGuildWarehouse(guildId);
        return null;
    }
}
