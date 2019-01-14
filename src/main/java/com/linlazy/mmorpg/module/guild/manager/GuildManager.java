//package com.linlazy.mmorpg.module.guild.manager;
//
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.module.guild.constants.GuildAuthLevel;
//import com.linlazy.mmorpg.module.guild.constants.GuildOpertorType;
//import com.linlazy.mmorpg.module.guild.dao.GuildActorDao;
//import com.linlazy.mmorpg.module.guild.dao.GuildDao;
//import com.linlazy.mmorpg.module.guild.dao.GuildOffLineDao;
//import com.linlazy.mmorpg.module.guild.dao.GuildWarehouseDao;
//import com.linlazy.mmorpg.module.guild.domain.GuildDo;
//import com.linlazy.mmorpg.module.guild.entity.Guild;
//import com.linlazy.mmorpg.module.guild.entity.GuildActor;
//import com.linlazy.mmorpg.module.guild.entity.GuildOffLine;
//import com.linlazy.mmorpg.module.guild.push.GuildPushHelper;
//import com.linlazy.mmorpg.server.common.Result;
//import com.linlazy.mmorpg.utils.SessionManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * @author linlazy
// */
//@Component
//public class GuildManager {
//
//
//    @Autowired
//    private GuildActorDao guildActorDao;
//    @Autowired
//    private GuildOffLineDao guildOffLineDao;
//    @Autowired
//    private GuildDao guildDao;
//    @Autowired
//    private GuildWarehouseDao guildWarehouseDao;
//
//    private Map<Long,Long> actorIdGuildIdMap = new ConcurrentHashMap<>();
//
//    private AtomicLong maxGuildId = new AtomicLong(0);
//
//    public boolean hasGuild(long actorId) {
//        return actorIdGuildIdMap.get(actorId) != null;
//    }
//
//    /**
//     * 创建公会
//     * @param actorId
//     * @return
//     */
//    public Result<?> createGuild(long actorId) {
//
//        long newGuildId = maxGuildId.incrementAndGet();
//        GuildActor guildActor = new GuildActor();
//        guildActor.setActorId(actorId);
//        guildActor.setGuildId(newGuildId);
//        guildActor.setAuthLevel(GuildAuthLevel.PRESIDENT);
//        guildActorDao.addGuildActor(guildActor);
//        actorIdGuildIdMap.put(actorId,newGuildId);
//        return Result.success();
//    }
//
//
//    /**
//     * 加入公会
//     * @param actorId
//     * @param guildId
//     * @return
//     */
//    public Result<?> applyJoin(long actorId, long guildId) {
//
//        GuildDo guildDo = getGuildDo(guildId);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("sourceId",actorId);
//        Set<Long> hasAuthInviteSet = guildDo.getHasAuthInviteSet();
//        for(Long targetId:hasAuthInviteSet){
//            //在线则直接推送
//            if(SessionManager.isOnline(targetId)){
//                GuildPushHelper.pushGuildOperator(targetId, GuildOpertorType.APPLY_JOIN,jsonObject);
//            }else {
//                //不在线则，先存档，下次登录时推送
//                GuildOffLine guildOffLine = new GuildOffLine();
//                guildOffLine.setGuild(guildId);
//                guildOffLine.setReceiver(targetId);
//                guildOffLine.setSourceId(actorId);
//                guildOffLine.setType(GuildOpertorType.APPLY_JOIN);
//                guildOffLineDao.addGuildOffLine(guildOffLine);
//            }
//        }
//        return Result.success();
//    }
//
//
//    public GuildDo getGuildDo(long guildId) {
//        GuildDo guildDo = new GuildDo();
//        guildDo.setGuildId(guildId);
//
//        Set<GuildActor> guildActorSet = guildActorDao.getGuildActorSet(guildId);
//        for(GuildActor guildActor : guildActorSet){
//            switch (guildActor.getAuthLevel()){
//                case GuildAuthLevel.PRESIDENT:
//                    guildDo.setPresidentId(guildActor.getActorId());
//                break;
//                case GuildAuthLevel.VICE_PRESIDENT:
//                    guildDo.getVoidPresidentIdSet().add(guildActor.getActorId());
//                break;
//                case GuildAuthLevel.EXCELLENT_MEMBER:
//                    guildDo.getExcellentMemberIdSet().add(guildActor.getActorId());
//                    break;
//                case GuildAuthLevel.ORDINARY_MEMBER:
//                    guildDo.getOrdinaryMemberIdSet().add(guildActor.getActorId());
//                    break;
//                default:
//                    guildDo.getNewMemberIdSet().add(guildActor.getActorId());
//            }
//        }
//
//        return guildDo;
//    }
//
//    public Result<?> acceptJoin(long actorId, long targetId) {
//        Long guildId = actorIdGuildIdMap.get(actorId);
//        actorIdGuildIdMap.put(actorId,guildId);
//        GuildActor guildActor = new GuildActor();
//        guildActor.setActorId(targetId);
//        guildActor.setGuildId(guildId);
//        guildActor.setAuthLevel(GuildAuthLevel.NEW_MEMBER);
//        guildActorDao.addGuildActor(guildActor);
//        return Result.success();
//    }
//
//    public Result<?> appoint(long actorId, long targetId, int authLevel) {
//        Long guildId = actorIdGuildIdMap.get(actorId);
//        GuildActor guildActor = guildActorDao.getGuildActor(guildId, targetId);
//        guildActor.setAuthLevel(authLevel);
//        guildActorDao.updateGuildActor(guildActor);
//        return Result.success();
//    }
//
//    public long getGuildId(long actorId) {
//        return actorIdGuildIdMap.get(actorId);
//    }
//
//    /**
//     * 踢出公会
//     * @param actorId
//     * @param targetId
//     * @return
//     */
//    public Result<?> shotOffGuild(long actorId, long targetId) {
//        Long guildId = actorIdGuildIdMap.remove(targetId);
//        GuildActor guildActor = guildActorDao.getGuildActor(guildId, targetId);
//        guildActorDao.deleteGuildActor(guildActor);
//        return Result.success();
//    }
//
//
//    /**
//     * 捐献金币
//     * @param actorId
//     * @param gold
//     * @return
//     */
//    public Result<?> donateGold(long actorId, int gold) {
//
//        Long guildId = actorIdGuildIdMap.get(actorId);
//        Guild guild = guildDao.getGuild(guildId);
//        guild.setGold(guild.getGold() + gold);
//        guildDao.updateGuild(guild);
//        return Result.success();
//    }
//
//    public Result<?> getGuildWareHouse(long actorId) {
//        Long guildId = actorIdGuildIdMap.get(actorId);
//        guildWarehouseDao.getGuildWarehouse(guildId);
//        return null;
//    }
//}
