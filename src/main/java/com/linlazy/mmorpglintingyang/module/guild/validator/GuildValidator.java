package com.linlazy.mmorpglintingyang.module.guild.validator;

import com.linlazy.mmorpglintingyang.module.guild.constants.GuildAuthLevel;
import com.linlazy.mmorpglintingyang.module.guild.dao.GuildDao;
import com.linlazy.mmorpglintingyang.module.guild.entity.Guild;
import com.linlazy.mmorpglintingyang.module.guild.manager.GuildManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GuildValidator {

    @Autowired
    private GuildManager guildManager;
    @Autowired
    private GuildDao guildDao;

    public boolean hasGuild(long actorId) {
        return guildManager.hasGuild(actorId);
    }


    public boolean hasAuth(long actorId,long targetId,int authLevel){
        long guildId =  guildManager.getGuildId(actorId);
        Guild actorGuild = guildDao.getGuild(guildId, actorId);
        Guild targetGuild = guildDao.getGuild(guildId, targetId);
        if(actorGuild.getAuthLevel() >= GuildAuthLevel.VicePresident
        && actorGuild.getAuthLevel() > targetGuild.getAuthLevel()
        && actorGuild.getAuthLevel() > authLevel){
            return true;
        }
        return false;
    }

    public boolean hasAuth(long actorId, long targetId) {
        long guildId =  guildManager.getGuildId(actorId);
        Guild actorGuild = guildDao.getGuild(guildId, actorId);
        Guild targetGuild = guildDao.getGuild(guildId, targetId);
        if(actorGuild.getAuthLevel() >= GuildAuthLevel.VicePresident
                && actorGuild.getAuthLevel() > targetGuild.getAuthLevel()){
            return true;
        }
        return false;
    }
}
