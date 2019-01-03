package com.linlazy.mmorpglintingyang.module.team.push;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.team.dto.TeamDTO;
import com.linlazy.mmorpglintingyang.server.common.PushHelper;

/**
 * @author linlazy
 */
public class TeamPushHelper {

    public static void pushTeam(long actorId, int teamOperationType) {
        JSONObject jsonObject = new JSONObject();
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setSourceId(actorId);
        teamDTO.setTeamOperatorType(teamOperationType);

        jsonObject.put("team", teamDTO);
        PushHelper.push(actorId,jsonObject);
    }

    public static void pushShotOffTeam(long actorId,long sourceId,long targetId, int teamOperationType) {
        JSONObject jsonObject = new JSONObject();
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setSourceId(sourceId);
        teamDTO.setTargetId(targetId);
        teamDTO.setTeamOperatorType(teamOperationType);
        jsonObject.put("team", teamDTO);
        PushHelper.push(actorId,jsonObject);
    }

}
