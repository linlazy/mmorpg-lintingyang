package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.entity.ChatEntity;
import com.linlazy.mmorpg.service.PlayerService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class ChatDTO {

    /**
     *  内容来源
     */
    Long sourceId;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 聊天类型
     */
    private Integer chatType;

    public ChatDTO(ChatEntity chat) {
        this.sourceId = chat.getSourceId();
        this.content = chat.getContent();
    }

    public ChatDTO() {

    }

    @Override
    public String toString() {
        PlayerService playerService = SpringContextUtil.getApplicationContext().getBean(PlayerService.class);
        Player player = playerService.getPlayer(sourceId);
        return String.format("玩家【%s】 对你说：【%s】",player.getName(),content);
    }
}
