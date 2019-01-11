package com.linlazy.mmorpg.module.email.entity;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class Email {

    /**
     * 邮件标识
     */
    private long emailId;

    /**
     * 邮件来源
     */
    private long sourceId;

    /**
     * 邮件接受者
     */
    private long receiver;

    /**
     * 邮件模板ID
     */
    private String emailTemplateId;

    /**
     * 标题参数
     */
    private String titleArgs;

    /**
     * 内容参数
     */
    private String contentArgs;

    /**
     * 奖励
     */
    private String rewards;
}
