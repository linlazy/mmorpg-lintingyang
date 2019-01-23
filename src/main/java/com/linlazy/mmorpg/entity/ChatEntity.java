package com.linlazy.mmorpg.entity;

import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
@Table("chat")
public class ChatEntity extends Entity {

    /**
     * 聊天标识
     */
    @Cloumn(pk = true)
    private long chatId;

    /**
     * 来源
     */
    @Cloumn
    private long sourceId;

    /**
     * 接受者
     */
    @Cloumn
    private long receiver;

    /**
     * 内容
     */
    @Cloumn
    private String content;



}
