package com.linlazy.mmorpglintingyang.module.guild.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.guild.service.GuildService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
@Module("guild")
public class GuildHandler {

    @Autowired
    private GuildService guildService;

    /**
     * 创建公会
     * @param jsonObject
     * @return
     */
    @Cmd("createGuild")
    public Result<?> createGuild(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return guildService.createGuild(actorId);
    }

    /**
     * 申请加入公会
     * @param jsonObject
     * @return
     */
    @Cmd("applyJoin")
    public Result<?> applyJoin(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long guildId = jsonObject.getLongValue("guildId");
        return guildService.applyJoin(actorId,guildId);
    }

    /**
     * 同意加入公会
     * @param jsonObject
     * @return
     */
    @Cmd("acceptJoin")
    public Result<?> acceptJoin(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return guildService.acceptJoin(actorId,targetId);
    }


    /**
     * 退出公会
     * @param jsonObject
     * @return
     */
    @Cmd("quitGuild")
    public Result<?> quitGuild(JSONObject jsonObject){
        return Result.success();
    }

    /**
     * 任命
     * @param jsonObject
     * @return
     */
    @Cmd("appoint")
    public Result<?> appoint(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        int authLevel = jsonObject.getIntValue("authLevel");
        return guildService.appoint(actorId,targetId,authLevel);
    }


    /**
     * 踢出公会
     * @param jsonObject
     * @return
     */
    @Cmd("shotOffGuild")
    public Result<?> shotOffGuild(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return guildService.shotOffGuild(actorId,targetId);
    }

    /**
     * 获取公会仓库信息
     * @param jsonObject
     * @return
     */
    @Cmd("getGuildWareHouse")
    public Result<?> getGuildWareHouse(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return guildService.getGuildWareHouse(actorId);
    }


    /**
     * 捐献金币
     * @param jsonObject
     * @return
     */
    @Cmd("donateGold")
    public Result<?> donateGold(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        int gold = jsonObject.getIntValue("gold");
        return guildService.donateGold(actorId,gold);
    }
}
