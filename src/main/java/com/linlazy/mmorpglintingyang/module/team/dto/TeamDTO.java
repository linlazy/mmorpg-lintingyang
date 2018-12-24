package com.linlazy.mmorpglintingyang.module.team.dto;

import lombok.Data;

@Data
public class TeamDTO {

    /**
     * 来源
     */
    private long sourceId;

    /**
     * 目标对象
     */
    private long targetId;

    /**
     * 操作类型
     */
    private int TeamOperatorType;
}
