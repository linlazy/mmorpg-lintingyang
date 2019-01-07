package com.linlazy.mmorpglintingyang.module.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 玩家任务信息
 * @author linlazy
 */
public class PlayerTaskInfo {

    /**
     * 玩家ID
     */
    private long actorId;
    /**
     * 玩家任务
     */
    private Set<Task> taskSet = new HashSet<>();
}
