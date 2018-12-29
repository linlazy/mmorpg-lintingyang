package com.linlazy.mmorpglintingyang.module.guild.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.stereotype.Component;

@Component
@Module("guild")
public class GuildHandler {

    /**
     * 创建公会
     * @param jsonObject
     * @return
     */
    @Cmd("createGuild")
    public Result<?> createGuild(JSONObject jsonObject){
        return Result.success();
    }

    /**
     * 加入公会
     * @param jsonObject
     * @return
     */
    @Cmd("joinGuild")
    public Result<?> joinGuild(JSONObject jsonObject){
        return Result.success();
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
        return Result.success();
    }


    /**
     * 踢出公会
     * @param jsonObject
     * @return
     */
    @Cmd("shotOffGuild")
    public Result<?> shotOffGuild(JSONObject jsonObject){
        return Result.success();
    }
}
