package com.linlazy.mmorpglintingyang.module.scene.domain;

import lombok.Data;

import java.util.List;

/**
 *  与NPC对话中的玩家
 */
@Data
public class NpcTalkPlayerDo {

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 对话
     */
    private long npcId;

    /**
     * 当前与NPC对话进度
     */
    private List<Integer> talkProcess;
}
