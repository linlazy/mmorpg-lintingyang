package com.linlazy.mmorpglintingyang.module.guild.service;

import com.linlazy.mmorpglintingyang.module.guild.manager.GuildManager;
import com.linlazy.mmorpglintingyang.module.guild.validator.GuildValidator;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GuildService {

    @Autowired
    private GuildValidator guildValidator;
    @Autowired
    private GuildManager guildManager;


    public Result<?> createGuild(long actorId) {
        if(guildValidator.hasGuild(actorId)){
            return Result.valueOf("已加入公会");
        }

        return guildManager.createGuild(actorId);
    }

    public Result<?> applyJoin(long actorId, long guildId) {
        if(guildValidator.hasGuild(actorId)){
            return Result.valueOf("已加入公会");
        }

        return guildManager.applyJoin(actorId,guildId);
    }

    public Result<?> acceptJoin(long actorId, long targetId) {


        return guildManager.acceptJoin(actorId,targetId);
    }

    /**
     * 任命
     * @param actorId
     * @param targetId
     * @param authLevel
     * @return
     */
    public Result<?> appoint(long actorId, long targetId, int authLevel) {
        if(!guildValidator.hasAuth(actorId,targetId,authLevel)){
            return Result.valueOf("无权限");
        }
        return guildManager.appoint(actorId,targetId,authLevel);
    }


    /**
     * 踢出公会
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> shotOffGuild(long actorId, long targetId) {
        if(!guildValidator.hasAuth(actorId,targetId)){
            return Result.valueOf("无权限");
        }
        return guildManager.shotOffGuild(actorId,targetId);
    }

    /**
     * 获取公会仓库信息
     * @param actorId
     * @return
     */
    public Result<?> getGuildWareHouse(long actorId) {

        return guildManager.getGuildWareHouse(actorId);
    }


    /**
     * 捐献金币
     * @param actorId
     * @param gold
     * @return
     */
    public Result<?> donateGold(long actorId, int gold) {

        return guildManager.donateGold(actorId,gold);
    }
}
