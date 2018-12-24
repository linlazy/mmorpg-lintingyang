package com.linlazy.mmorpglintingyang.module.email.dto;

import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import lombok.Data;

import java.util.List;

@Data
public class EmailDTO {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 附件奖励
     */
    private List<Reward> attachment;
}
