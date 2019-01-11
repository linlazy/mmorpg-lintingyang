package com.linlazy.mmorpg.module.email.dto;

import com.linlazy.mmorpg.module.common.reward.Reward;
import lombok.Data;

import java.util.List;

/**
 * @author linlazy
 */
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
