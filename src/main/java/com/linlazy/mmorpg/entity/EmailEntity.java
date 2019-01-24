package com.linlazy.mmorpg.entity;

import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
@Table("email")
public class EmailEntity extends Entity {

    /**
     * 邮件标识
     */
    @Cloumn(pk = true)
    private long emailId;

    /**
     * 邮件发送者
     */
    @Cloumn
    private long senderId;

    /**
     * 邮件类型
     */
    @Cloumn
    private int type;

    /**
     * 邮件接受者
     */
    @Cloumn
    private long receiverId;


    /**
     * 标题
     */
    @Cloumn
    private String title;

    /**
     * 读取状态
     */
    @Cloumn
    private boolean readStatus;
    /**
     * 领奖状态
     */
    @Cloumn
    private boolean rewardStatus;
    /**
     * 内容
     */
    @Cloumn
    private String content;

    /**
     * 奖励
     */
    @Cloumn
    private String rewards;

    /**
     * 发送时间
     */
    @Cloumn
    private long sendTime;

    /**
     * 过期时间
     */
    @Cloumn
    private long expireTime;
}
