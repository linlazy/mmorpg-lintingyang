package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 组队领域类
 * @author linlazy
 */
@Data
public class Team {

    /**
     * 队伍ID
     */
    private long teamId;



    private int maxNum = SpringContextUtil.getApplicationContext().getBean(GlobalConfigService.class).getGuildPackageMaxLatticeNum();

    /**
     * 队伍玩家信息
     */
    private Map<Long,PlayerTeamInfo> playerTeamInfoMap = new ConcurrentHashMap<>();


    /**
     *  是否人员已满
     * @return
     */
    public boolean isFull(){
        return playerTeamInfoMap.values().size() == maxNum;
    }


    public boolean hasJoinedTeam(long actorId) {
        return false;
    }
}
