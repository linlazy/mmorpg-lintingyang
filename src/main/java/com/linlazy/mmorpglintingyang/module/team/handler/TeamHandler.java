package com.linlazy.mmorpglintingyang.module.team.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.team.service.TeamService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Module("team")
public class TeamHandler {

    @Autowired
    private TeamService teamService;

    /**
     * 邀请加入
     * @param jsonObject
     * @return
     */
    @Cmd("inviteJoin")
    public Result<?> inviteJoin(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return teamService.inviteJoin(actorId,targetId);
    }

    /**
     * 拒绝邀请
     * @param jsonObject
     * @return
     */
    @Cmd("rejectJoin")
    public Result<?> rejectJoin(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return teamService.rejectJoin(actorId,targetId);
    }

    /**
     * 同意加入
     * @param jsonObject
     * @return
     */
    @Cmd("acceptJoin")
    public Result<?> acceptJoin(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return teamService.acceptJoin(actorId,targetId);
    }

    /**
     *  任命队长
     * @param jsonObject
     * @return
     */
    @Cmd("appointCaptain")
    public Result<?> appointCaptain(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return teamService.appointCaptain(actorId,targetId);
    }


    /**
     *  离开队伍
     * @param jsonObject
     * @return
     */
    @Cmd("leave")
    public Result<?> leave(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return teamService.leave(actorId);
    }

    /**
     *  踢出队伍
     * @param jsonObject
     * @return
     */
    @Cmd("shotOff")
    public Result<?> shotOff(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return teamService.shotOff(actorId,targetId);
    }
}
