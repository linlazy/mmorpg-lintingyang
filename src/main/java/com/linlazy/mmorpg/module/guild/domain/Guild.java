package com.linlazy.mmorpg.module.guild.domain;

import com.linlazy.mmorpg.entity.GuildEntity;
import com.linlazy.mmorpg.module.guild.constants.GuildAuthLevel;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 公会
 * @author linlazy
 */
@Data
public class Guild {

    /**
     * 公会ID
     */
    private long guildId;

    /**
     * 公会等级
     */
    private int level;

    /**
     * 公会金币
     */
    private long gold;


    /**
     * 公会仓库
     */
    private GuildWarehouse guildWarehouse;

    /**
     * 公会玩家
     */
    private Map<Long,GuildPlayer> guildPlayerMap = new HashMap<>();

    /**
     * 有权限邀请的公会玩家
     * @return
     */
    public Set<GuildPlayer> getHasAuthInvitePlayerSet(){
       return guildPlayerMap.values()
                .stream()
                .filter(guildPlayer -> guildPlayer.getAuthLevel() >= GuildAuthLevel.EXCELLENT_MEMBER)
                .collect(Collectors.toSet());
    }

    public GuildEntity convertGuildEntity() {
        return null;
    }
}
