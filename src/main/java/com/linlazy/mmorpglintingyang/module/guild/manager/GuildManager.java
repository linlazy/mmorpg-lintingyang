package com.linlazy.mmorpglintingyang.module.guild.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.guild.constants.GuildAuthLevel;
import com.linlazy.mmorpglintingyang.module.guild.constants.GuildOpertorType;
import com.linlazy.mmorpglintingyang.module.guild.dao.GuildDao;
import com.linlazy.mmorpglintingyang.module.guild.dao.GuildOffLineDao;
import com.linlazy.mmorpglintingyang.module.guild.domain.GuildDo;
import com.linlazy.mmorpglintingyang.module.guild.entity.Guild;
import com.linlazy.mmorpglintingyang.module.guild.entity.GuildOffLine;
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
    private GuildDao guildDao;
    @Autowired
    private GuildOffLineDao guildOffLineDao;

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
        Guild guild = new Guild();
        guild.setActorId(actorId);
        guild.setGuildId(newGuildId);
        guild.setAuthLevel(GuildAuthLevel.President);
        guildDao.addGuild(guild);
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

        Set<Guild> guildSet = guildDao.getGuildSet(guildId);
        for(Guild guild: guildSet){
            switch (guild.getAuthLevel()){
                case GuildAuthLevel.President:
                    guildDo.setPresidentId(guild.getActorId());
                break;
                case GuildAuthLevel.VicePresident:
                    guildDo.getVoidPresidentIdSet().add(guild.getActorId());
                break;
                case GuildAuthLevel.ExcellentMember:
                    guildDo.getExcellentMemberIdSet().add(guild.getActorId());
                    break;
                case GuildAuthLevel.OrdinaryMember:
                    guildDo.getOrdinaryMemberIdSet().add(guild.getActorId());
                    break;
                default:
                    guildDo.getNewMemberIdSet().add(guild.getActorId());
            }
        }

        return guildDo;
    }

    public Result<?> acceptJoin(long actorId, long targetId) {
        Long guildId = actorIdGuildIdMap.get(actorId);
        actorIdGuildIdMap.put(actorId,guildId);
        Guild guild = new Guild();
        guild.setActorId(targetId);
        guild.setGuildId(guildId);
        guild.setAuthLevel(GuildAuthLevel.NewMember);
        guildDao.addGuild(guild);
        return Result.success();
    }

    public Result<?> appoint(long actorId, long targetId, int authLevel) {
        Long guildId = actorIdGuildIdMap.get(actorId);
        Guild guild = guildDao.getGuild(guildId, targetId);
        guild.setAuthLevel(authLevel);
        guildDao.updateGuild(guild);
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
        Guild guild = guildDao.getGuild(guildId, targetId);
        guildDao.deleteGuild(guild);
        return Result.success();
    }
}
