package com.linlazy.mmorpg.module.guild.domain;

import com.linlazy.mmorpg.entity.GuildPlayerEntity;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

/**
 * 公会玩家领域类
 * @author linlazy
 */
@Data
public class GuildPlayer {

    /**
     * 公会ID
     */
    private long guildId;
    /**
     * 玩家
     */
    private Player player;

    /**
     * 公会权限
     */
    private int authLevel;

    public GuildPlayer(GuildPlayerEntity guildPlayerEntity) {
        PlayerService playerService = SpringContextUtil.getApplicationContext().getBean(PlayerService.class);
        guildId = guildPlayerEntity.getGuildId();
        player = playerService.getPlayer(guildPlayerEntity.getActorId());
        authLevel = guildPlayerEntity.getAuthLevel();
    }

    public GuildPlayerEntity convertGuildPlayerEntity() {
        GuildPlayerEntity guildPlayerEntity = new GuildPlayerEntity();
        guildPlayerEntity.setActorId(player.getActorId());
        guildPlayerEntity.setAuthLevel(authLevel);
        guildPlayerEntity.setGuildId(guildId);

        return guildPlayerEntity;
    }
}
