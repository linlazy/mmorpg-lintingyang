package com.linlazy.mmorpglintingyang.module.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 技能领域类
 * @author linlazy
 */
public class Skill {

    /**
     * 技能ID
     */
    private int skillId;

    /**
     * 等级
     */
    private int level;

    /**
     * 是否已穿戴
     */
    private boolean dress;

    /**
     * 下一次CDH恢复时间
     */
    private long nextCDResumeTimes;

    /**
     * 玩家技能映射
     */
    private Map<Long,PlayerSkillInfo> playerSkillInfoMap = new HashMap<>();


    /**
     * 获取玩家技能信息
     * @param actorId
     * @return
     */
    public PlayerSkillInfo getPlayerSkillInfo(long actorId) {
        return playerSkillInfoMap.get(actorId);
    }
}
